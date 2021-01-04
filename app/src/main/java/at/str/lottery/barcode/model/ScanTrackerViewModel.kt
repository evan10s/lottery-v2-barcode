package at.str.lottery.barcode.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.str.lottery.barcode.ui.TAG
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScanTrackerViewModel : ViewModel() {
    private val _state = MutableStateFlow(BarcodeScreenState())

    private val lastBarcodeScanned = MutableStateFlow("")
    private val sendingData = MutableStateFlow(false)
    private val sendError = MutableStateFlow("")
    private val kioskConfig: MutableStateFlow<KioskConfig?> = MutableStateFlow(null)

    val state: StateFlow<BarcodeScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                    lastBarcodeScanned,
                    sendingData,
                    sendError,
                    kioskConfig
            ) { lastBarcodeScanned, sendingData, sendError, kioskConfig ->
                BarcodeScreenState(
                        lastBarcodeScanned = lastBarcodeScanned,
                        sendingData = sendingData,
                        sendError = sendError,
                        kioskConfig = kioskConfig
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it

                Log.i(TAG, "VIEW MODEL INITIALIZED! Kiosk ID is ${kioskConfig.value?.kioskId}")
                Log.i(TAG, "This is where the websocket would be initialized, if that were a thing")

            }
        }
    }

    fun onBarcodeScanned(barcodes: List<Barcode>) {
        if (barcodes.isEmpty()) {
            return
        }

        val barcode = barcodes[0].displayValue
        if (barcode != lastBarcodeScanned.value) {
            lastBarcodeScanned.value = barcode!!
            if (KioskConfig.isKioskConfigBarcode(barcode)) {
                Log.i(TAG, "Wow, this is a KioskConfig barcode!")

                val decodeResult = KioskConfig.decodeKioskConfigBarcode(barcode)

                kioskConfig.value = decodeResult
            } else {
                viewModelScope.launch {
                    Log.i(
                        TAG,
                        "This is where I'd send the barcode $barcode to the websocket, if that were a thing"
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ScanTracker ViewModel destroyed!")
    }
}

data class BarcodeScreenState(
        val lastBarcodeScanned: String = "",
        val sendingData: Boolean = false,
        val sendError: String = "",
        val kioskConfig: KioskConfig? = KioskConfig()
)