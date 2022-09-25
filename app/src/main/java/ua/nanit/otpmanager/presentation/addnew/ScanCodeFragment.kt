package ua.nanit.otpmanager.presentation.addnew

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragScanBinding
import ua.nanit.otpmanager.domain.QrImageReader
import ua.nanit.otpmanager.presentation.ext.display
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

@AndroidEntryPoint
class ScanCodeFragment : Fragment() {

    companion object {
        private const val PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var binding: FragScanBinding

    private val viewModel: AddViewModel by viewModels()
    private var executor: ExecutorService? = null
    private var analyzerBlocked = AtomicBoolean(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()

        if (!hasCamera()) {
            showCloseableSnackbar(R.string.scan_camera_missing)
            navController.navigateUp()
            return
        }

        executor = Executors.newSingleThreadExecutor()

        if (isGrantedPermissions()) {
            openCamera()
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    openCamera()
                } else {
                    showCloseableSnackbar(R.string.scan_permission_required)
                    navController.navigateUp()
                }
            }.launch(PERMISSION)
        }

        viewModel.success.observe(viewLifecycleOwner) {
            val msg = getString(R.string.accounts_add_success, it.name)
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
            navController.navigateUp()
        }

        viewModel.error.observe(viewLifecycleOwner) {
            showCloseableSnackbar(it.display(requireContext()))
            navController.navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor?.shutdown()
    }

    private fun openCamera() {
        val ctx = requireContext()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
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
        }, ContextCompat.getMainExecutor(ctx))
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
        ContextCompat.checkSelfPermission(requireContext(), PERMISSION) ==
                PackageManager.PERMISSION_GRANTED

    private fun hasCamera(): Boolean =
        requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

}