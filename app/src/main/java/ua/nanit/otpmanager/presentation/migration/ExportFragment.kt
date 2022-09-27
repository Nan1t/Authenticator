package ua.nanit.otpmanager.presentation.migration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragExportBinding
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar
import ua.nanit.otpmanager.service.FileExportService

class ExportFragment : Fragment() {

    private lateinit var binding: FragExportBinding
    private lateinit var writePermLauncher: ActivityResultLauncher<String>
    private val pinCodeDialog = lazy { PinCodeDialog(requireContext(), R.string.account_export_pin_info) {
        exportFile(it)
    }}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragExportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.exportFile.setOnClickListener { openPinDialog() }
        binding.exportQr.setOnClickListener { navigator().navToExportQr() }

        writePermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openPinDialog()
            } else {
                showCloseableSnackbar(R.string.account_export_permission)
            }
        }
    }

    private fun openPinDialog() {
        if (isWritingPermsGranted()) {
            pinCodeDialog.value.show()
        } else {
            writePermLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun exportFile(pin: String) {
        val intent = Intent(requireContext(), FileExportService::class.java).apply {
            putExtra(FileExportService.EXTRA_PIN_CODE, pin)
        }

        requireContext().startService(intent)
        showCloseableSnackbar(R.string.account_export_started)
        navigator().navUp()
    }

    private fun isWritingPermsGranted(): Boolean =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED

}