package ua.nanit.otpmanager.presentation.accounts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragAccountsBinding
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.presentation.ext.navigator

@AndroidEntryPoint
class AccountsFragment : AccountListener, Fragment() {

    private val viewModel: AccountsViewModel by viewModels()
    private val renameDialog = lazy { AccountRenameDialog(requireContext()) {
        viewModel.edit(it)
    }}
    private val deleteDialog = lazy { AccountDeleteDialog(requireContext()) {
        viewModel.delete(it)
    }}

    private lateinit var binding: FragAccountsBinding
    private lateinit var clipboardManager: ClipboardManager

    private var fabEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

        val navigator = navigator()
        val adapter = AccountsAdapter(this)
        val menuProvider = AccountsMainMenu(navigator)

        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
        enableFab(false)

        binding.accountsList.layoutManager = LinearLayoutManager(requireContext())
        binding.accountsList.adapter = adapter
        binding.addAccountBtn.setOnClickListener {
            enableFab(!fabEnabled)
        }

        binding.addManualBtn.setOnClickListener {
            enableFab(false)
            navigator.navToManualAdd()
        }

        binding.addScanBtn.setOnClickListener {
            enableFab(false)
            navigator.navToScanCode()
        }

        viewModel.updateResult.observe(viewLifecycleOwner, adapter::update)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.accounts.collect {
                    adapter.updateAll(it)
                }
            }
        }
    }

    override fun onCopy(password: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("otp", password))
        Snackbar.make(requireView(), R.string.accounts_copied, Snackbar.LENGTH_SHORT).show()
    }

    override fun onMenuClick(account: Account, anchor: View) {
        AccountContextMenu(requireContext(), anchor, account,
            renameDialog.value, deleteDialog.value).show()
    }

    override fun onHotpIncrement(acc: Account) {
        viewModel.edit(acc)
    }

    private fun enableFab(enabled: Boolean) {
        fabEnabled = enabled

        if (enabled) {
            binding.addManualBtn.show()
            binding.addManualText.visibility = View.VISIBLE
            binding.addScanBtn.show()
            binding.addScanText.visibility = View.VISIBLE
        } else {
            binding.addManualBtn.hide()
            binding.addManualText.visibility = View.GONE
            binding.addScanBtn.hide()
            binding.addScanText.visibility = View.GONE
        }
    }
}