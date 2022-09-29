package ua.nanit.otpmanager.presentation.accounts

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.domain.account.Account

class AccountContextMenu(
    ctx: Context,
    anchor: View,
    private val account: Account,
    private val renameDialog: AccountRenameDialog,
    private val deleteDialog: AccountDeleteDialog
) : PopupMenu(ctx, anchor) {

    override fun show() {
        inflate(R.menu.editor)
        setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuEdit -> {
                    renameDialog.show(account)
                    true
                }
                R.id.menuDelete -> {
                    deleteDialog.show(account)
                    true
                }
                else -> false
            }
        }
        super.show()
    }

}