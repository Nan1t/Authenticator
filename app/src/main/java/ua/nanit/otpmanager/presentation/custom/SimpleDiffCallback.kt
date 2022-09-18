package ua.nanit.otpmanager.presentation.custom

import androidx.recyclerview.widget.DiffUtil

open class SimpleDiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val comparator: (T, T) -> Boolean
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return comparator(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}