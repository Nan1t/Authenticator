package ua.nanit.otpmanager.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ua.nanit.otpmanager.databinding.ItemAccountBinding
import ua.nanit.otpmanager.domain.Account
import ua.nanit.otpmanager.domain.code

class AccountsAdapter(
    private val data: List<Account>
) : RecyclerView.Adapter<AccountHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return AccountHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}

class AccountHolder(
    private val binding: ItemAccountBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(acc: Account) {
        binding.accountName.text = acc.name
        binding.accountCode.text = acc.code()
    }

}