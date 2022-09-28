package ua.nanit.otpmanager.presentation.migration.file

import android.Manifest
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

@AndroidEntryPoint
class ImportFileFragment : FileMigrationFragment() {

    private val viewModel: FileMigrationViewModel by viewModels()
    private val pinDialog = lazy {
        PinDialog(
            requireContext(),
            R.string.account_import_file_pin,
            onConfirm = { pin ->
                setProcessing(true)
                //viewModel.export(pin)
            },
            onCancel = { navigator().navUp() }
        )
    }

    override val permission: String = Manifest.permission.READ_EXTERNAL_STORAGE

    override fun onPermissionResult(result: Boolean) {
        if (result) {
            pinDialog.value.show()
        } else {
            showCloseableSnackbar(R.string.account_import_permission)
        }
    }

    private fun import(pin: String) {
        if (isPermissionGranted()) {
            // TODO viewModel.import()
        } else {
            requestPermission()
        }
    }

}