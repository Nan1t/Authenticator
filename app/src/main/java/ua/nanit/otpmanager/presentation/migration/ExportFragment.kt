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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragExportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.exportFile.setOnClickListener { exportFile() }
        binding.exportQr.setOnClickListener { navigator().navToExportQr() }

        writePermLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                exportFile()
            } else {
                showCloseableSnackbar(R.string.account_export_permission)
            }
        }
    }

    private fun exportFile() {
        if (isWritingPermsGranted()) {
            requireContext().startService(Intent(requireContext(), FileExportService::class.java))
            showCloseableSnackbar(R.string.account_export_started)
            navigator().navUp()
        } else {
            writePermLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun isWritingPermsGranted(): Boolean =
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED

}