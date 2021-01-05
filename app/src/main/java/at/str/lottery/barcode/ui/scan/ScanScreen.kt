package at.str.lottery.barcode.ui.scan

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.NavController
import at.str.lottery.barcode.R
import at.str.lottery.barcode.model.*
import at.str.lottery.barcode.ui.theme.danger
import at.str.lottery.barcode.ui.theme.green
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
            .padding(12.dp),
        elevation = 6.dp
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (viewState.scannerMode) {
                ScannerMode.SETUP -> {
                    when {
                        viewState.sendingData -> {
                            StatusText(
                                header = { CircularProgressIndicator() },
                                title = "Sending data...",
                            )
                        }
                        viewState.lastScanResult?.message?.isNotBlank() == true -> {
                            StatusText(
                                header = { ErrorIcon() },
                                title = "Scan error",
                                description = viewState.lastScanResult.message
                            )
                        }
                        else -> {
                            StatusText(
                                header = { CircularProgressIndicator() },
                                title = "Scan kiosk configuration barcode",
                                description = "You can find this on the initial kiosk setup screen"
                            )
                        }
                    }
                }
                ScannerMode.READY -> {
                    when {
                        viewState.sendingData -> {
                            StatusText(
                                header = { CircularProgressIndicator() },
                                title = "Sending data...",
                            )
                        }
                        viewState.lastScanResult?.success == true -> {
                            StatusText(
                                header = { CheckmarkIcon() },
                                title = "Scan success",
                                description = viewState.lastScanResult.data
                            )
                        }
                        viewState.lastScanResult?.success != true -> {
                            viewState.lastScanResult?.let {
                                StatusText(
                                    header = { ErrorIcon() },
                                    title = "Scan error",
                                    description = it.message
                                )
                            }
                        }
                        else -> {
                            StatusText(
                                header = { CheckmarkIcon() },
                                title = "Ready to scan"
                            )
                        }
                    }
                }
                ScannerMode.WAITING_FOR_FIRST_TICKET -> {
                    StatusText(
                        header = { CheckmarkIcon() },
                        title = "Ready to scan tickets"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusText(
    header: @Composable () -> Unit,
    title: String,
    description: String = "",
) {
    header()
    Text(
        text = title,
        style = MaterialTheme.typography.h6
    )
    if (description.isNotBlank()) {
        Text(
            text = description,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ErrorIcon() {
    Icon(
        imageVector = vectorResource(id = R.drawable.ic_baseline_cancel_40_red),
        tint = danger,
    )
}

@Composable
fun CheckmarkIcon() {
    Icon(
        // Increase size method - https://stackoverflow.com/a/64378616
        imageVector = Icons.Filled.CheckCircle.copy(defaultHeight = 40.dp, defaultWidth = 40.dp),
        tint = green,
    )
}

@Preview
@Composable
fun ScanInstructionsPreviewScanSuccess() {
    ScanInstructions(BarcodeScreenState(
        lastScanResult = BarcodeScanResult(data = "ABC123", success = true),
        scannerMode = ScannerMode.READY,
        kioskConfig = KioskConfig("ABCDEF", Uri.parse("https://example.com"))
    ))
}

@Preview
@Composable
fun ScanInstructionsPreviewScanError() {
    ScanInstructions(BarcodeScreenState(
        lastScanResult = BarcodeScanResult(data = "ABC123", success = false, message = "Invalid barcode"),
        scannerMode = ScannerMode.READY,
        kioskConfig = KioskConfig("ABCDEF", Uri.parse("https://example.com"))
    ))
}

@Preview
@Composable
fun ScanInstructions() {
    ScanInstructions(
        BarcodeScreenState(
            kioskConfig = null,
        )
    )
}
