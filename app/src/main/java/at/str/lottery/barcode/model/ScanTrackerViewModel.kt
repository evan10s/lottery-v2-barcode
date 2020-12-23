package at.str.lottery.barcode.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import at.str.lottery.barcode.ui.TAG

class ScanTrackerViewModel : ViewModel() {
    private lateinit var barcodesScanned: MutableMap<String, Int>

    init {
        Log.i(TAG, "ScanTracker ViewModel created!")
        barcodesScanned = mutableMapOf()
    }

    fun recordScan(barcode: String) {
        barcodesScanned.putIfAbsent(barcode, 0)
        barcodesScanned[barcode] = barcodesScanned[barcode]!! + 1

        if (barcodesScanned[barcode]!! > 5) {
            Log.i(TAG, "confirmed barcode! ${barcode}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ScanTracker ViewModel destroyed!")
    }
}