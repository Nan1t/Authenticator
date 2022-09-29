package ua.nanit.otpmanager.presentation.accounts

import android.os.Handler
import android.os.Looper
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
import ua.nanit.otpmanager.domain.time.TotpTimer
import ua.nanit.otpmanager.presentation.custom.SimpleDiffCallback

class AccountsAdapter(
    private val listener: AccountListener
) : RecyclerView.Adapter<AccountsAdapter.AccountHolder>() {

    private val handler = Handler(Looper.getMainLooper())
    private var data: List<Account> = emptyList()

    fun updateAll(newData: List<Account>) {
        val callback = SimpleDiffCallback(data, newData) { old, new -> old.label == new.label }
        val result = DiffUtil.calculateDiff(callback, false)
        data = newData
        result.dispatchUpdatesTo(this)
    }

    fun update(acc: Account) {
        for (i in data.indices) {
            if (acc == data[i]) {
                notifyItemChanged(i)
                return
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return AccountHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class AccountHolder(
        private val binding: ItemAccountBinding
    ) : RecyclerView.ViewHolder(binding.root), TotpListener {

        private var holden: TotpAccount? = null
        private var lastSecond: Int = 0

        init {
            TotpTimer.subscribe(this)
        }

        fun bind(acc: Account) {
            holden = null
            lastSecond = 0

            when (acc) {
                is TotpAccount -> bindTotp(acc)
                is HotpAccount -> bindHotp(acc)
            }

            binding.accountName.text = if (acc.issuer != null) "${acc.issuer} (${acc.name})" else acc.name
            binding.password.text = acc.password.formatAsOtp()
            itemView.setOnClickListener { listener.onCopy(acc.password) }
            itemView.setOnLongClickListener { listener.onMenuClick(acc, it); true }
        }

        override fun onTick() {
            holden?.let { account ->
                val remain = account.secondsRemain()

                if (remain > lastSecond) {
                    account.update()
                    handler.post { binding.password.text = account.password.formatAsOtp() }
                }

                lastSecond = remain
                handler.post {
                    binding.progressBar.setProgressCompat(remain - 1, true)
                    binding.remainTime.text = remain.toString()
                }
            }
        }

        private fun bindTotp(acc: TotpAccount) {
            binding.remainTime.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.refreshBtn.visibility = View.GONE
            binding.progressBar.max = acc.interval.toInt() - 1

            lastSecond = acc.secondsRemain()
            holden = acc
            acc.update()
            onTick()
        }

        private fun bindHotp(acc: HotpAccount) {
            binding.remainTime.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.refreshBtn.visibility = View.VISIBLE
            binding.refreshBtn.setOnClickListener {
                if (acc.increment()) {
                    binding.password.text = acc.password.formatAsOtp()
                    listener.onHotpIncrement(acc)
                    notifyItemChanged(adapterPosition)
                }
            }
        }
    }
}