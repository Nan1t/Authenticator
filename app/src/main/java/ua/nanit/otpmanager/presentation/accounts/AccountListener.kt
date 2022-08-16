package ua.nanit.otpmanager.presentation.accounts

import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.HotpAccount

interface AccountListener {

    fun onUpdate(account: AccountWrapper)

    fun onCopy(password: String)

    fun onSelect(account: Account)

}