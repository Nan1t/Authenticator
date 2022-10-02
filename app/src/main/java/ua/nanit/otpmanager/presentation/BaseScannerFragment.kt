package ua.nanit.otpmanager.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragScanBinding
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseScannerFragment : Fragment(), BarcodeCallback {

    companion object {
        private const val PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var binding: FragScanBinding
    private lateinit var vibrator: Vibrator
    private lateinit var captureManager: CaptureManager

    private val locker = AtomicBoolean(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!hasCamera()) {
            showCloseableSnackbar(R.string.scan_camera_missing)
            navigator().navUp()
            return
        }

        vibrator = requireContext().getSystemService() ?: return
        captureManager = CaptureManager(requireActivity(), binding.barcodeScanner)

        if (isGrantedPermissions()) {
            openCamera()
        } else {
            val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    openCamera()
                } else {
                    showCloseableSnackbar(R.string.scan_permission_required)
                    navigator().navUp()
                }
            }

            if (savedInstanceState == null) {
                launcher.launch(PERMISSION)
            }
        }
    }

    override fun barcodeResult(result: BarcodeResult) {
        if (locker.get()) return

        if (result.text != null) {
            locker.set(true)
            vibrateCompat()
            processUri(result.text)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }

    protected abstract fun processUri(uri: String)

    protected fun unblockScanner() {
        locker.set(false)
    }

    private fun openCamera() {
        binding.barcodeScanner.barcodeView.decoderFactory =
            DefaultDecoderFactory(listOf(BarcodeFormat.QR_CODE))
        binding.barcodeScanner.decodeContinuous(this)
    }

    private fun vibrateCompat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            vibrator.vibrate(100L)
        }
    }

    private fun isGrantedPermissions(): Boolean =
        ContextCompat.checkSelfPermission(requireContext(), PERMISSION) ==
                PackageManager.PERMISSION_GRANTED

    private fun hasCamera(): Boolean =
        requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

}