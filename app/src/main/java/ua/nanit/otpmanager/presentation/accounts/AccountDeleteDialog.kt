package ua.nanit.otpmanager.presentation.accounts

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.DialogAccountDeleteBinding
import ua.nanit.otpmanager.domain.account.Account

class AccountDeleteDialog(
    private val ctx: Context,
    private val onConfirm: (Account) -> Unit
) : AlertDialog(ctx) {

    private val binding = DialogAccountDeleteBinding.inflate(LayoutInflater.from(ctx), null, false)

    init {
        setTitle(R.string.account_delete_title)
        setView(binding.root)
        setButton(BUTTON_POSITIVE, ctx.getString(R.string.account_delete)) { _, _ -> }
        setButton(BUTTON_NEGATIVE, ctx.getString(R.string.cancel)) { _, _ -> }
    }

    fun show(acc: Account) {
        binding.text.text = ctx.getString(R.string.account_delete_warning, acc.name)
        setOnShowListener { dialog ->
            getButton(BUTTON_POSITIVE).setOnClickListener {
                onConfirm(acc)
                dialog.dismiss()
            }
        }
        show()
    }

}