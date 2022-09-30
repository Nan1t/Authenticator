package ua.nanit.otpmanager.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragProgressBinding
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

abstract class BaseScannerFragment : Fragment() {

    companion object {
        private const val PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var binding: FragProgressBinding
    private lateinit var scannerLauncher: ActivityResultLauncher<ScanOptions>
    private lateinit var navigator: Navigator
    private lateinit var vibrator: Vibrator
    private lateinit var options: ScanOptions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        vibrator = requireContext().getSystemService() ?: return
        navigator = navigator()

        options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt(getString(R.string.scan_hint))
        options.setBarcodeImageEnabled(false)
        options.setOrientationLocked(false)
        options.setBeepEnabled(false)

        scannerLauncher = registerForActivityResult(ScanContract()) {
            if (it.contents != null) {
                vibrateCompat()
                processUri(it.contents)
            } else {
                navigator.navUp()
            }
        }

        if (!hasCamera()) {
            showCloseableSnackbar(R.string.scan_camera_missing)
            navigator.navUp()
            return
        }

        if (isGrantedPermissions()) {
            openScanner()
        } else {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    openScanner()
                } else {
                    showCloseableSnackbar(R.string.scan_permission_required)
                    navigator.navUp()
                }
            }.launch(PERMISSION)
        }
    }

    protected abstract fun processUri(uri: String)

    protected fun openScanner() {
        scannerLauncher.launch(options)
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