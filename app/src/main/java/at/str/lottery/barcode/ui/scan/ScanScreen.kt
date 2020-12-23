package at.str.lottery.barcode.ui.scan

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import at.str.lottery.barcode.model.ScanTrackerViewModel
import at.str.lottery.barcode.ui.TAG
import com.google.mlkit.vision.barcode.Barcode

fun handleScan(barcodes: List<Barcode>, scanTrackerViewModel: ScanTrackerViewModel) {
    barcodes.forEach {
        scanTrackerViewModel.recordScan(it.displayValue ?: "")
    }
    Log.i(TAG, barcodes.joinToString { it -> it.displayValue ?: "" })
}

@Composable
fun ScanScreen(navController: NavController, scanTrackerViewModel: ScanTrackerViewModel) {
    Column(Modifier.fillMaxHeight()) {
        CameraPreview(::handleScan, scanTrackerViewModel)
    }
}
