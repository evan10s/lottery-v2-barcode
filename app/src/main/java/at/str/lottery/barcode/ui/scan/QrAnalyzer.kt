package at.str.lottery.barcode.ui.scan

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

internal class QrAnalyzer(private val listener: (List<Barcode>) -> Unit) : ImageAnalysis.Analyzer {
    val TAG = "QrAnalyzer"

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val scannerOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(scannerOptions)

            scanner.process(image)
                .addOnSuccessListener(listener)
                .addOnFailureListener {
                    Log.e(TAG, "Failure processing image for barcodes", it)
                }
                .addOnCompleteListener { imageProxy.close() }
        }
    }
}