package ua.nanit.otpmanager.presentation.accounts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.appComponent
import ua.nanit.otpmanager.databinding.FragAccountsBinding
import ua.nanit.otpmanager.domain.account.Account
import javax.inject.Inject

class AccountsFragment : AccountListener, Fragment() {

    private val viewModel: AccountsViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragAccountsBinding
    private lateinit var clipboardManager: ClipboardManager

    @Inject
    lateinit var viewModelFactory: AccountsViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activateMainMenu()

        clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

        val adapter = AccountsAdapter(this, emptyList())

        binding.accountsList.layoutManager = LinearLayoutManager(requireContext())
        binding.addAccountBtn.setOnClickListener { onAddBtnClick() }
        binding.accountsList.adapter = adapter

        viewModel.accounts.observe(viewLifecycleOwner) {
            adapter.updateAll(it)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updates.collect {
                adapter.update(it)
                println("Update ${it.account.name}")
            }
        }
    }

    override fun onUpdate(account: AccountWrapper) {
        viewModel.updateAccount(account)
    }

    override fun onCopy(password: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("otp", password))
        Snackbar.make(requireView(), R.string.accounts_copied, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSelect(account: Account) {
        //activateEditorMenu()
    }

    private fun onAddBtnClick() {
        findNavController().navigate(R.id.actionNavAddManual)
    }

    private fun activateMainMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun activateEditorMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.editor, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}