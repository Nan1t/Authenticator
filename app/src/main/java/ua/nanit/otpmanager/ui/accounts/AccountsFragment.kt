package ua.nanit.otpmanager.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ua.nanit.otpmanager.databinding.FragAccountsBinding
import ua.nanit.otpmanager.domain.Account

class AccountsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragAccountsBinding.inflate(inflater, container, false)
//        val list = ArrayList<Account>()
//
//        for (i in 0..15) {
//            list.add(Account("Account $i", "token"))
//        }
//
//        val adapter = AccountsAdapter(list)
//
//        binding.accountsList.layoutManager = LinearLayoutManager(requireContext())
//        binding.accountsList.adapter = adapter

        return binding.root
    }

}