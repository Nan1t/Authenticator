package ua.nanit.otpmanager.presentation.accounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.nanit.otpmanager.databinding.ItemAccountBinding
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.HotpAccount
import ua.nanit.otpmanager.domain.account.TotpAccount
import ua.nanit.otpmanager.domain.otp.formatAsOtp
import ua.nanit.otpmanager.domain.time.TotpTimer
import ua.nanit.otpmanager.presentation.custom.SimpleDiffCallback

class AccountsAdapter(
    private val listener: AccountListener,
    private var data: List<Account>,
) : RecyclerView.Adapter<AccountsAdapter.AccountHolder>() {

    fun updateAll(newData: List<Account>) {
        val callback = SimpleDiffCallback(data, newData) { old, new ->
            old.label == new.label
        }
        val result = DiffUtil.calculateDiff(callback, false)
        data = newData
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return AccountHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onViewDetachedFromWindow(holder: AccountHolder) {
        TotpTimer.unsubscribe(holder.adapterPosition)
    }

    override fun getItemCount(): Int = data.size

    inner class AccountHolder(
        private val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(acc: Account) {
            binding.accountName.text = acc.name
            binding.password.text = acc.password.formatAsOtp()
            binding.root.setOnClickListener { listener.onCopy(acc.password) }
            binding.root.setOnLongClickListener { listener.onMenuClick(acc, it); true }

            when (acc) {
                is TotpAccount -> bind(acc)
                is HotpAccount -> bind(acc)
            }
        }

        private fun bind(acc: TotpAccount) {
            binding.progressBar.visibility = View.VISIBLE
            binding.refreshBtn.visibility = View.GONE
            binding.progressBar.progress = acc.progress(1000)

            TotpTimer.subscribe(adapterPosition) {
                binding.root.post {
                    binding.progressBar.progress = acc.progress(1000)
                }
                if (acc.update()) {
                    binding.root.post { binding.password.text = acc.password.formatAsOtp() }
                }
            }
        }

        private fun bind(acc: HotpAccount) {
            binding.progressBar.visibility = View.GONE
            binding.refreshBtn.visibility = View.VISIBLE
            binding.refreshBtn.setOnClickListener { println("Update HOTP") }
        }
    }
}