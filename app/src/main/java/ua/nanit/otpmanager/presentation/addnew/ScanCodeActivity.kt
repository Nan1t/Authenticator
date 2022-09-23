package ua.nanit.otpmanager.presentation.addnew

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.ActivityScanBinding
import ua.nanit.otpmanager.domain.QrImageReader
import ua.nanit.otpmanager.presentation.ext.display
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

@AndroidEntryPoint
class ScanCodeActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 0
        private const val PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var binding: ActivityScanBinding

    private val viewModel: AddViewModel by viewModels()
    private var executor: ExecutorService? = null
    private var analyzerBlocked = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        if (!hasCamera()) {
            Toast.makeText(this, "No camera!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        executor = Executors.newSingleThreadExecutor()

        if (isGrantedPermissions()) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION), PERMISSION_REQUEST_CODE)
        }

        viewModel.success.observe(this) {
            val msg = getString(R.string.accounts_add_success, it.name)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it.display(this), Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (isGrantedPermissions()) {
                openCamera()
            } else {
                Toast.makeText(this, R.string.scan_permission_required, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor?.shutdown()
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val executor = executor ?: return

        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
                .setImageQueueDepth(1)
                .build()
            val viewSize = max(binding.cameraPreview.width, binding.cameraPreview.height)
            val frameSize = binding.frame.width // Assume it's a square (it must)

            preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            analyzer.setAnalyzer(executor) {
                analyzeFrame(viewSize, frameSize, it)
            }

            try {
                provider.unbindAll()
                provider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA,
                    preview, analyzer)
            } catch(ex: Exception) {
                Log.e(null, "Camera use cases binding failed", ex)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun analyzeFrame(viewSize: Int, frameSize: Int, img: ImageProxy) {
        if (!analyzerBlocked.get()) {
            val yBuffer = img.planes[0].buffer
            val vuBuffer = img.planes[2].buffer

            val ySize = yBuffer.remaining()
            val vuSize = vuBuffer.remaining()

            val yuvBytes = ByteArray(ySize + vuSize)

            yBuffer.get(yuvBytes, 0, ySize)
            vuBuffer.get(yuvBytes, ySize, vuSize)

            val multiplier = max(img.width, img.height).toFloat() / viewSize
            val resizedFrame = (frameSize * multiplier).toInt()
            val frameX = img.width / 2 - resizedFrame / 2
            val frameY = img.height / 2 - resizedFrame / 2
            val uri = QrImageReader.read(yuvBytes, img.width, img.height, frameX, frameY, resizedFrame)

            if (uri != null) {
                analyzerBlocked.set(true)
                viewModel.createByUri(uri)
            }
        }

        img.close()
    }

    private fun isGrantedPermissions(): Boolean =
        ContextCompat.checkSelfPermission(this, PERMISSION) ==
                PackageManager.PERMISSION_GRANTED

    private fun hasCamera(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
}