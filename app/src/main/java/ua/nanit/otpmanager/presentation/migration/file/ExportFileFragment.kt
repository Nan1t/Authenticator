package ua.nanit.otpmanager.presentation.migration.file

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.domain.migration.FileMigration
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

@AndroidEntryPoint
class ExportFileFragment : FileMigrationFragment() {

    private val viewModel: FileMigrationViewModel by viewModels()
    private val pinDialog by lazy {
        PinDialog(
            requireContext(),
            R.string.account_export_file_pin,
            onConfirm = { pin ->
                viewModel.savePin(pin)
                openFileDialog()
            },
            onCancel = { navigator().navUp() }
        )
    }
    private lateinit var documentLauncher:  ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProcessing(false)

        documentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                setProcessing(true)
                it.data?.data?.let { uri ->
                    viewModel.export(uri)
                }
            }
        }

        pinDialog.show()

        viewModel.observeExportResult(viewLifecycleOwner) { filename ->
            setProcessing(false)
            showCloseableSnackbar(getString(R.string.account_export_file_success, filename))
            navigator().navUpToMain()
        }

        viewModel.observeErrorResult(viewLifecycleOwner) { msg ->
            showCloseableSnackbar(getString(R.string.error, msg))
            navigator().navUpToMain()
        }
    }

    override fun onPermissionResult(result: Boolean) {
        if (result) {
            pinDialog.show()
        } else {
            showCloseableSnackbar(R.string.account_export_permission)
            navigator().navUp()
        }
    }

    private fun openFileDialog() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/${FileMigration.FILE_EXT}"
            putExtra(Intent.EXTRA_TITLE, "${FileMigration.FILE_NAME}.${FileMigration.FILE_EXT}")
        }

        documentLauncher.launch(intent)
    }
}