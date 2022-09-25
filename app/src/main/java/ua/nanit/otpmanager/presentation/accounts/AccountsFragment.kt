package ua.nanit.otpmanager.presentation.accounts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragAccountsBinding
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.presentation.export.AccountExporter

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
    private lateinit var exporter: AccountExporter

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
        val navController = findNavController()
        clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        exporter = AccountExporter(this)

        setupMainMenu()
        enableFab(false)

        val adapter = AccountsAdapter(this)

        binding.accountsList.layoutManager = LinearLayoutManager(requireContext())
        binding.accountsList.adapter = adapter
        binding.addAccountBtn.setOnClickListener {
            enableFab(!fabEnabled)
        }

        binding.addManualBtn.setOnClickListener {
            enableFab(false)
            navController.navigate(R.id.actionNavAddManual)
        }

        binding.addScanBtn.setOnClickListener {
            enableFab(false)
            navController.navigate(R.id.actionNavScanCode)
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
        val menu = PopupMenu(requireContext(), anchor)
        menu.inflate(R.menu.editor)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuEdit -> {
                    renameDialog.value.show(account)
                    true
                }
                R.id.menuDelete -> {
                    deleteDialog.value.show(account)
                    true
                }
                else -> false
            }
        }
        menu.show()
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

    private fun setupMainMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menuImport -> {
                        true
                    }
                    R.id.menuExport -> {
                        exporter.export()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }
}