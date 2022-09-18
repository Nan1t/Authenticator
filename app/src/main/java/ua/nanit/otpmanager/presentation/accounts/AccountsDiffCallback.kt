package ua.nanit.otpmanager.presentation.accounts

import androidx.recyclerview.widget.DiffUtil
import ua.nanit.otpmanager.domain.account.Account

class AccountsDiffCallback(
    private val oldList: List<Account>,
    private val newList: List<Account>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].label == newList[newItemPosition].label
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}