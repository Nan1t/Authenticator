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
import ua.nanit.otpmanager.domain.time.TotpListener
import ua.nanit.otpmanager.presentation.custom.SimpleDiffCallback

class AccountsAdapter(
    private val listener: AccountListener
) : RecyclerView.Adapter<AccountsAdapter.AccountHolder>() {

    private var data: List<Account> = emptyList()

    fun updateAll(newData: List<Account>) {
        val callback = SimpleDiffCallback(data, newData) { old, new -> old.label == new.label }
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
        holder.unbind(data[holder.adapterPosition])
    }

    override fun onViewAttachedToWindow(holder: AccountHolder) {
        holder.bind(data[holder.adapterPosition])
    }

    override fun getItemCount(): Int = data.size

    inner class AccountHolder(
        val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(acc: Account) {
            binding.accountName.text = acc.name
            binding.password.text = acc.password.formatAsOtp()
            binding.root.setOnClickListener { listener.onCopy(acc.password) }
            binding.root.setOnLongClickListener { listener.onMenuClick(acc, it); true }

            when (acc) {
                is TotpAccount -> bindTotp(acc)
                is HotpAccount -> bindHotp(acc)
            }
        }

        fun unbind(acc: Account) {
            if (acc is TotpAccount)
                acc.removeListener()
        }

        private fun bindTotp(acc: TotpAccount) {
            binding.progressBar.visibility = View.VISIBLE
            binding.refreshBtn.visibility = View.GONE

            binding.progressBar.max = acc.interval.toInt() - 1
            binding.progressBar.setProgressCompat(acc.secondsRemain(), false)

            acc.listen(object : TotpListener {
                override fun onTick(progress: Int) {
                    itemView.post {
                        binding.progressBar.setProgressCompat(progress, true)
                    }
                }

                override fun onUpdate() {
                    itemView.post {
                        binding.password.text = acc.password.formatAsOtp()
                    }
                }
            })
        }

        private fun bindHotp(acc: HotpAccount) {
            binding.progressBar.visibility = View.GONE
            binding.refreshBtn.visibility = View.VISIBLE
            binding.refreshBtn.setOnClickListener {
                if (acc.increment()) {
                    binding.password.text = acc.password.formatAsOtp()
                    listener.onHotpIncrement(acc)
                }
            }
        }
    }
}