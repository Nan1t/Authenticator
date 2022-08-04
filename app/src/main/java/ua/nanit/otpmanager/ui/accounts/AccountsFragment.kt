package ua.nanit.otpmanager.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragAccountsBinding

class AccountsFragment : Fragment() {

    private lateinit var binding: FragAccountsBinding

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
    }

    private fun onAddBtnClick(view: View) {
        findNavController().navigate(R.id.navAddManual)
    }

}