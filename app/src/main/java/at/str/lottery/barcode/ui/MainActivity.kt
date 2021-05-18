package at.str.lottery.barcode.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import at.str.lottery.barcode.databinding.CameraHostBinding
import at.str.lottery.barcode.model.ScanTrackerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val TAG = "LotteryBarcode"

class MainActivity : ComponentActivity() {
    private lateinit var cameraHostBinding: CameraHostBinding
    private lateinit var viewModel: ScanTrackerViewModel

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraHostBinding = CameraHostBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ScanTrackerViewModel::class.java)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS)
        }

        setContent {
            LotteryApp(viewModel)
        }
    }

    private fun takePhoto() {}

    private fun startCamera() {}

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
//        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
