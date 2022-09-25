package ua.nanit.otpmanager.domain.storage

import ua.nanit.otpmanager.domain.account.Account

interface AccountStorage {

    fun getAll(): List<Account>

    fun get(label: String): Account?

    fun add(account: Account)

    fun edit(account: Account)

    fun delete(account: Account)

    fun export(): String

}