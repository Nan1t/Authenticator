package ua.nanit.otpmanager.presentation.migration.file

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
                viewModel.import(pin)
            },
            onCancel = { navigator().navUp() }
        )
    }

    override val permission: String = Manifest.permission.READ_EXTERNAL_STORAGE

    lateinit var fileDialog: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProcessing(false)

        fileDialog = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                val stream = requireContext().contentResolver.openInputStream(uri)

                if (stream != null) {
                    viewModel.selectFile(stream)
                    return@registerForActivityResult
                }
            }

            showCloseableSnackbar(R.string.account_import_file_error)
            navigator().navUp()
        }

        if (savedInstanceState == null) {
            if (isPermissionGranted()) {
                openFileDialog()
            } else {
                requestPermission()
            }
        }

        viewModel.errorResult.observe(viewLifecycleOwner) { msg ->
            showCloseableSnackbar(getString(R.string.error, msg))
            navigator().navUp()
        }

        viewModel.fileResult.observe(viewLifecycleOwner) {
            pinDialog.value.show()
        }

        viewModel.importResult.observe(viewLifecycleOwner) { count ->
            showCloseableSnackbar(getString(R.string.account_import_success, count))
            navigator().navUpToMain()
        }
    }

    override fun onPermissionResult(result: Boolean) {
        if (result) {
            openFileDialog()
        } else {
            navigator().navUp()
            showCloseableSnackbar(R.string.account_import_permission)
        }
    }

    private fun openFileDialog() {
        fileDialog.launch(arrayOf("application/zip"))
    }
}