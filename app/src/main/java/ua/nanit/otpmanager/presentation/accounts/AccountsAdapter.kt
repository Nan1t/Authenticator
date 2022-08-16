package ua.nanit.otpmanager.presentation.accounts

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ua.nanit.otpmanager.databinding.ItemAccountBinding
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.HotpAccount
import ua.nanit.otpmanager.domain.account.TotpAccount
import ua.nanit.otpmanager.domain.otp.formatAsOtp

class AccountsAdapter(
    private val listener: AccountListener,
    private var data: List<Account>,
) : RecyclerView.Adapter<AccountHolder>() {

    fun updateAll(newData: List<Account>) {
        val result = DiffUtil.calculateDiff(AccountsDiffCallback(data, newData), false)
        data = newData
        result.dispatchUpdatesTo(this)
    }

    fun update(wrapper: AccountWrapper) {
        notifyItemChanged(wrapper.position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(inflater, parent, false)
        return AccountHolder(listener, binding)
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}

class AccountHolder(
    private val listener: AccountListener,
    private val binding: ItemAccountBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(acc: Account) {
        binding.accountName.text = acc.name
        binding.accountCode.text = acc.currentPassword.formatAsOtp()

        itemView.setOnClickListener {
            listener.onCopy(acc.currentPassword)
        }

        itemView.setOnLongClickListener {
            listener.onSelect(acc)
            true
        }

        when (acc) {
            is TotpAccount -> bindTotp(acc)
            is HotpAccount -> bindHotp(acc)
        }
    }

    private fun bindTotp(acc: TotpAccount) {
        binding.progressBar.visibility = View.VISIBLE
        binding.refreshBtn.visibility = View.GONE
        val currentProgress = (acc.secondsToUpdate() * 1000 / acc.interval).toInt()

        ValueAnimator.ofInt(currentProgress, 0).apply {
            duration = acc.secondsToUpdate() * 1000
            interpolator = LinearInterpolator()
            doOnEnd {
                listener.onUpdate(AccountWrapper(acc, adapterPosition))
            }
            addUpdateListener {
                binding.progressBar.progress = it.animatedValue as Int
            }
        }.start()
    }

    private fun bindHotp(acc: HotpAccount) {
        binding.progressBar.visibility = View.GONE
        binding.refreshBtn.visibility = View.VISIBLE
        binding.refreshBtn.setOnClickListener {
            listener.onUpdate(AccountWrapper(acc, adapterPosition))
        }
    }

}