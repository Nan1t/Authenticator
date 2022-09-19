package ua.nanit.otpmanager.presentation.accounts

import android.view.View
import ua.nanit.otpmanager.domain.account.Account

interface AccountListener {

    fun onCopy(password: String)

    fun onMenuClick(account: Account, anchor: View)

    fun onUpdate(acc: Account)

}