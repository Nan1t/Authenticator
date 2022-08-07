package ua.nanit.otpmanager.presentation.accounts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.appComponent
import ua.nanit.otpmanager.databinding.FragAccountsBinding
import javax.inject.Inject

class AccountsFragment : Fragment() {

    private val viewModel: AccountsViewModel by viewModels { viewModelFactory }
    private val adapter = AccountsAdapter(emptyList())

    private lateinit var binding: FragAccountsBinding

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
        binding.accountsList.layoutManager = LinearLayoutManager(requireContext())
        binding.addAccountBtn.setOnClickListener(::onAddBtnClick)
        binding.accountsList.adapter = adapter

        viewModel.accounts.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
    }

    private fun onAddBtnClick(view: View) {
        findNavController().navigate(R.id.actionNavAddManual)
    }

}