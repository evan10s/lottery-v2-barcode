package at.str.lottery.barcode.ui.scan

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContextCompat
import at.str.lottery.barcode.databinding.CameraHostBinding
import at.str.lottery.barcode.ui.TAG
import com.google.mlkit.vision.barcode.Barcode
import java.util.concurrent.Executors


@Composable
fun CameraPreview(onBarcodeScanned: (List<Barcode>) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // In Jetpack Compose world this is kinda cheating and just bringing in a ConstraintLayout
    // with the CameraX preview component in it.  But it's the only option right now.
    AndroidViewBinding(CameraHostBinding::inflate) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val cameraExecutor = Executors.newSingleThreadExecutor()

            val imageQrAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrAnalyzer { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            onBarcodeScanned(barcodes)
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageQrAnalyzer)
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }
}
