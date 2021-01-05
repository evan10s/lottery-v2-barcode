package at.str.lottery.barcode.model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.str.lottery.barcode.ui.TAG
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScanTrackerViewModel : ViewModel() {
    private val _state = MutableStateFlow(BarcodeScreenState())

    private val lastScanResult: MutableStateFlow<BarcodeScanResult?> = MutableStateFlow(null)
    private val sendingData = MutableStateFlow(false)
    private val sendError = MutableStateFlow("")
    private val kioskConfig: MutableStateFlow<KioskConfig?> = MutableStateFlow(null)
    private val scannerMode = MutableStateFlow(ScannerMode.SETUP)

    val state: StateFlow<BarcodeScreenState>
        get() = _state

    init {
        Log.i(TAG, "VIEW MODEL INITIALIZED! Kiosk ID is ${kioskConfig.value?.kioskId}")

        viewModelScope.launch {
            combine(
                    lastScanResult,
                    sendingData,
                    sendError,
                    kioskConfig,
                    scannerMode
            ) { lastScanResult, sendingData, sendError, kioskConfig, scannerMode ->
                BarcodeScreenState(
                        lastScanResult = lastScanResult,
                        sendingData = sendingData,
                        sendError = sendError,
                        kioskConfig = kioskConfig,
                        scannerMode = scannerMode
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    fun onBarcodeScanned(barcodes: List<Barcode>) {
        if (barcodes.isEmpty()) {
            lastScanResult.value = BarcodeScanResult("", false, "Empty barcode")
            return
        }
        val barcode = barcodes[0].displayValue

        if (barcode.isNullOrBlank()) {
            lastScanResult.value = BarcodeScanResult("", false, "Empty barcode")
            return
        }

        if ((barcode == lastScanResult.value?.data && lastScanResult.value?.isRecent() == true)
            || sendingData.value) {
            return
        }

        if (scannerMode.value == ScannerMode.WAITING_FOR_FIRST_TICKET) {
            scannerMode.value = ScannerMode.READY
        }

        if (scannerMode.value == ScannerMode.SETUP) {
            if (KioskConfig.isKioskConfigBarcode(barcode)) {
                sendingData.value = true

                viewModelScope.launch {
                    delay(2000)

                    val decodeResult = KioskConfig.decodeKioskConfigBarcode(barcode)

                    kioskConfig.value = decodeResult

                    if (kioskConfig.value?.isValid() == true) {
                        Log.i(TAG, "Kiosk config is valid")
                        lastScanResult.value = BarcodeScanResult(barcode, true)
                        scannerMode.value = ScannerMode.WAITING_FOR_FIRST_TICKET
                    } else {
                        lastScanResult.value = BarcodeScanResult(barcode, false, "Invalid kiosk configuration barcode")
                    }

                    sendingData.value = false
                }
            } else {
                lastScanResult.value = BarcodeScanResult(barcode, false, "That doesn't look like a kiosk configuration barcode")
            }
        } else if (scannerMode.value == ScannerMode.READY) {
            sendingData.value = true

            if (KioskConfig.isKioskConfigBarcode(barcode)) {
                lastScanResult.value = BarcodeScanResult(barcode, false, "Kiosk config cannot be changed at this time")
                sendingData.value = false
                return
            }

            viewModelScope.launch {
                delay(2000)
                lastScanResult.value = BarcodeScanResult(barcode, true)
                sendingData.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ScanTracker ViewModel destroyed!")
    }
}

enum class ScannerMode {
    SETUP, WAITING_FOR_FIRST_TICKET, READY
}

data class BarcodeScreenState(
    val lastScanResult: BarcodeScanResult? = null,
    val sendingData: Boolean = false,
    val sendError: String = "",
    val kioskConfig: KioskConfig? = null,
    val scannerMode: ScannerMode = ScannerMode.SETUP
)