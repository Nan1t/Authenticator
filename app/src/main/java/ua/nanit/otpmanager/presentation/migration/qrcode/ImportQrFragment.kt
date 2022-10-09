package ua.nanit.otpmanager.presentation.migration.qrcode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.BaseScannerFragment
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

@AndroidEntryPoint
class ImportQrFragment : BaseScannerFragment() {

    private val viewModel: QrMigrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeErrorResult(viewLifecycleOwner) {
            showCloseableSnackbar(getString(R.string.error, it))
            navigator().navUp()
        }

        viewModel.observeImportResult(viewLifecycleOwner) { result ->
            showCloseableSnackbar(getString(R.string.account_import_success, result.count))

            if (result.last) {
                navigator().navUpToMain()
            } else {
                unblockScanner()
            }
        }
    }

    override fun processUri(uri: String) {
        viewModel.import(uri)
    }
}