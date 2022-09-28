package ua.nanit.otpmanager.presentation.migration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragMigrationBinding
import ua.nanit.otpmanager.presentation.ext.navigator

abstract class MigrationFragment : Fragment() {

    protected lateinit var binding: FragMigrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragMigrationBinding.inflate(inflater, container, false)
        return binding.root
    }
}

class ExportFragment : MigrationFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.aboutText.setText(R.string.account_export_about)
        binding.fileBtn.setText(R.string.account_export_file)
        binding.qrCodeBtn.setText(R.string.account_export_qrcode)
        binding.fileBtn.setOnClickListener { navigator().navToExportFile() }
        binding.qrCodeBtn.setOnClickListener { navigator().navToExportQr() }
    }
}

class ImportFragment : MigrationFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.aboutText.setText(R.string.account_import_about)
        binding.fileBtn.setText(R.string.account_import_file)
        binding.qrCodeBtn.setText(R.string.account_import_qrcode)
        binding.fileBtn.setOnClickListener { navigator().navToImportFile() }
        binding.qrCodeBtn.setOnClickListener { navigator().navToImportQr() }
    }
}