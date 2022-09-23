package ua.nanit.otpmanager.presentation.accounts

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.DialogAccountRenameBinding
import ua.nanit.otpmanager.domain.account.Account

class AccountRenameDialog(
    ctx: Context,
    private val onConfirm: (Account) -> Unit
) : AlertDialog(ctx) {

    private val errorText = ctx.getString(R.string.error_shortLabel)
    private val binding = DialogAccountRenameBinding.inflate(LayoutInflater.from(ctx), null, false)

    init {
        setTitle(R.string.account_rename_title)
        setView(binding.root)
        setButton(BUTTON_POSITIVE, ctx.getString(R.string.account_rename)) { _, _ -> }
        setButton(BUTTON_NEGATIVE, ctx.getString(R.string.cancel)) { _, _ -> }
    }

    fun show(acc: Account) {
        binding.accountName.setText(acc.name)
        setOnShowListener { dialog ->
            binding.accountNameLayout.error = null

            getButton(BUTTON_POSITIVE).setOnClickListener {
                val name = binding.accountName.text.toString()

                if (name.length >= 3) {
                    acc.name = name
                    onConfirm(acc)
                    dialog.dismiss()
                } else {
                    binding.accountNameLayout.error = errorText
                }
            }
        }
        show()
    }

}