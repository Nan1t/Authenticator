package ua.nanit.otpmanager.presentation.accounts

import ua.nanit.otpmanager.domain.account.Account

class AccountWrapper(
    val account: Account,
    val position: Int
)