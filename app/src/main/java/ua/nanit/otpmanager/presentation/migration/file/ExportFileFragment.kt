package ua.nanit.otpmanager.presentation.migration.file

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

@AndroidEntryPoint
class ExportFileFragment : FileMigrationFragment() {

    private val viewModel: FileMigrationViewModel by viewModels()
    private val pinDialog = lazy {
        PinDialog(
            requireContext(),
            R.string.account_export_file_pin,
            onConfirm = { pin ->
                setProcessing(true)
                viewModel.export(pin)
            },
            onCancel = { navigator().navUp() }
        )
    }

    override val permission: String = Manifest.permission.WRITE_EXTERNAL_STORAGE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProcessing(false)

        if (isPermissionGranted()) {
            pinDialog.value.show()
        } else {
            requestPermission()
        }

        viewModel.exportResult.observe(viewLifecycleOwner) { filename ->
            setProcessing(false)
            showCloseableSnackbar(getString(R.string.account_export_file_success, filename))
            navigator().navUpToMain()
        }

        viewModel.errorResult.observe(viewLifecycleOwner) { msg ->
            showCloseableSnackbar(getString(R.string.error, msg))
            navigator().navUpToMain()
        }
    }

    override fun onPermissionResult(result: Boolean) {
        if (result) {
            pinDialog.value.show()
        } else {
            showCloseableSnackbar(R.string.account_export_permission)
        }
    }
}