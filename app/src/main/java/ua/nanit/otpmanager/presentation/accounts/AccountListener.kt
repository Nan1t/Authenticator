package ua.nanit.otpmanager.presentation.accounts

import ua.nanit.otpmanager.domain.account.Account

interface AccountListener {

    fun onUpdate(account: AccountItem)

    fun onCopy(password: String)

    fun onSelect(account: Account)

}