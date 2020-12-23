package at.str.lottery.barcode.ui.scan

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import at.str.lottery.barcode.ui.TAG
import com.google.mlkit.vision.barcode.Barcode

fun handleScan(barcodes: List<Barcode>) {
    barcodes.forEach {

    }
    Log.i(TAG, barcodes.joinToString { it -> it.displayValue ?: "" })
}

@Composable
fun ScanScreen(navController: NavController) {
    Column(Modifier.fillMaxHeight()) {
        CameraPreview(::handleScan)
    }
}
