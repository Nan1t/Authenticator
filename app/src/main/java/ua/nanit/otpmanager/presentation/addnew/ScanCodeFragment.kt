package ua.nanit.otpmanager.presentation.addnew

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.BaseScannerFragment
import ua.nanit.otpmanager.presentation.ext.display
import ua.nanit.otpmanager.presentation.ext.navigator
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

@AndroidEntryPoint
class ScanCodeFragment : BaseScannerFragment() {

    private val viewModel: AddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.success.observe(viewLifecycleOwner) {
            val msg = getString(R.string.accounts_add_success, it.name)
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
            navigator().navUp()
        }

        viewModel.error.observe(viewLifecycleOwner) {
            showCloseableSnackbar(it.display(requireContext()))
            navigator().navUp()
        }
    }

    override fun processUri(uri: String) {
        viewModel.createByUri(uri)
    }
}
