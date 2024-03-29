package at.str.lottery.barcode.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.str.lottery.barcode.ui.TAG
import at.str.lottery.barcode.util.KioskLink
import com.google.mlkit.vision.barcode.common.Barcode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ScanTrackerViewModel : ViewModel() {
    private val _state = MutableStateFlow(BarcodeScreenState())

    private val lastScanResult: MutableStateFlow<BarcodeScanResult?> = MutableStateFlow(null)
    private val sendingData = MutableStateFlow(false)
    private val sendError = MutableStateFlow("")
    private val kioskConfig: MutableStateFlow<KioskConfig?> = MutableStateFlow(null)
    private val scannerMode = MutableStateFlow(ScannerMode.SETUP)
    private val numScans = MutableStateFlow(0)
    private val kioskLink: MutableStateFlow<KioskLink?> = MutableStateFlow(null)

    val state: StateFlow<BarcodeScreenState>
        get() = _state

    init {
        Log.i(TAG, "VIEW MODEL INITIALIZED! Kiosk ID is ${kioskConfig.value?.kioskId}")

        viewModelScope.launch {
            combine(
                listOf(
                    lastScanResult,
                    sendingData,
                    sendError,
                    kioskConfig,
                    scannerMode,
                    numScans,
                    kioskLink,
                )
            ) { flows ->
                BarcodeScreenState(
                    lastScanResult = flows[0] as BarcodeScanResult?,
                    sendingData = flows[1] as Boolean,
                    sendError = flows[2] as String,
                    kioskConfig = flows[3] as KioskConfig?,
                    scannerMode = flows[4] as ScannerMode,
                    numScans = flows[5] as Int,
                    kioskLink = flows[6] as KioskLink?,
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
            || sendingData.value
        ) {
            return
        }

        if (scannerMode.value == ScannerMode.WAITING_FOR_FIRST_TICKET) {
            scannerMode.value = ScannerMode.READY
        }

        if (scannerMode.value == ScannerMode.SETUP) {
            if (KioskConfig.isKioskConfigBarcode(barcode)) {
                sendingData.value = true

                viewModelScope.launch {
                    val decodeResult = KioskConfig.decodeKioskConfigBarcode(barcode)

                    kioskConfig.value = decodeResult

                    if (kioskConfig.value?.isValid() == true) {
                        Log.i(TAG, "Kiosk config is valid")

                        kioskLink.value = KioskLink()
                        kioskLink.value?.run(
                            kioskConfig.value?.serverUrl.toString(),
                            kioskConfig.value?.kioskId.orEmpty()
                        )

                        lastScanResult.value = BarcodeScanResult(barcode, true)
                        scannerMode.value = ScannerMode.WAITING_FOR_FIRST_TICKET
                    } else {
                        lastScanResult.value =
                            BarcodeScanResult(barcode, false, "Invalid kiosk configuration barcode")
                    }

                    sendingData.value = false
                }
            } else {
                lastScanResult.value = BarcodeScanResult(
                    barcode,
                    false,
                    "That doesn't look like a kiosk configuration barcode"
                )
            }
        } else if (scannerMode.value == ScannerMode.READY) {
            sendingData.value = true

            if (KioskConfig.isKioskConfigBarcode(barcode)) {
                val decodeResult = KioskConfig.decodeKioskConfigBarcode(barcode)
                val same = decodeResult == kioskConfig.value

                when (same) {
                    true -> {
                        viewModelScope.launch {
                            lastScanResult.value =
                                BarcodeScanResult(barcode, true, "Retrying WebSocket connection...")
                            kioskLink.value = KioskLink()
                            kioskLink.value?.run(
                                kioskConfig.value?.serverUrl.toString(),
                                kioskConfig.value?.kioskId.orEmpty()
                            )

                            lastScanResult.value = BarcodeScanResult(barcode, true)
                            scannerMode.value = ScannerMode.WAITING_FOR_FIRST_TICKET
                        }

                        sendingData.value = false
                    }
                    false -> {
                        lastScanResult.value = BarcodeScanResult(
                            barcode,
                            false,
                            "Kiosk config cannot be changed at this time"
                        )
                        sendingData.value = false
                    }
                }
            } else {
                numScans.value += 1

                viewModelScope.launch {
                    if (kioskLink.value == null) {
                        lastScanResult.value = BarcodeScanResult(
                            barcode,
                            false,
                            "Unable to send data: not connected to websocket"
                        )
                    } else {
                        kioskLink.value?.sendBarcode(barcode)
                        lastScanResult.value = BarcodeScanResult(barcode, true)
                    }
                    sendingData.value = false
                }
            }
        }
    }

    fun reconnectToWebSocket() {
        kioskLink.value?.run(
            kioskConfig.value?.serverUrl.toString(),
            kioskConfig.value?.kioskId.orEmpty()
        )
    }

    fun onUpdateServerUrl(serverUrl: Uri) {
        kioskConfig.value?.serverUrl = serverUrl
    }

    fun onUpdateKioskId(kioskId: String) {
        kioskConfig.value?.kioskId = kioskId
    }

    fun setKioskLink(link: KioskLink?) {
        kioskLink.value = link
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
    val scannerMode: ScannerMode = ScannerMode.SETUP,
    val numScans: Int = 0,
    val kioskLink: KioskLink? = null,
)