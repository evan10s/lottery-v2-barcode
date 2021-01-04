package at.str.lottery.barcode.ui.scan

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavController
import at.str.lottery.barcode.model.BarcodeScreenState
import at.str.lottery.barcode.model.KioskConfig
import at.str.lottery.barcode.model.ScanTrackerViewModel
import at.str.lottery.barcode.ui.theme.green
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Composable
fun ScanScreen(navController: NavController, scanTrackerViewModel: ScanTrackerViewModel = viewModel()) {
    val viewState by scanTrackerViewModel.state.collectAsState()

    Box(Modifier.fillMaxHeight()) {
        CameraPreview(scanTrackerViewModel::onBarcodeScanned)
        ScanInstructions(viewState)
    }
}

@Composable
private fun ScanInstructions(
    viewState: BarcodeScreenState
) {
    Card(
        Modifier
            .alpha(0.8f)
            .fillMaxWidth()
//            .align(Alignment.TopCenter)
            .padding(12.dp),
        elevation = 6.dp
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewState.kioskConfig?.isValid() == true) {
                Icon(
                    // Increase size method - https://stackoverflow.com/a/64378616
                    imageVector = Icons.Filled.CheckCircle.copy(defaultHeight = 40.dp, defaultWidth = 40.dp),
                    tint = green,

                    )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Ready",
                    style = MaterialTheme.typography.h6
                )
            } else {
                CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
                Text(
                    "Scan kiosk configuration barcode",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Preview
@Composable
fun ScanInstructionsPreviewReady() {
    ScanInstructions(BarcodeScreenState(
        kioskConfig = KioskConfig("ABCDEF", Uri.parse("https://example.com"))
    ))
}

@Preview
@Composable
fun ScanInstructionsPreviewNotReady() {
    ScanInstructions(BarcodeScreenState(
        kioskConfig = null)
    )
}
