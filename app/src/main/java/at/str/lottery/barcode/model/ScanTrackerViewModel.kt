package at.str.lottery.barcode.model

import android.util.Log
import androidx.lifecycle.ViewModel
import at.str.lottery.barcode.ui.TAG

class ScanTrackerViewModel : ViewModel() {
    init {
        Log.i(TAG, "ScanTracker ViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ScanTracker ViewModel destroyed!")
    }
}