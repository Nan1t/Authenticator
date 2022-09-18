package ua.nanit.otpmanager.domain.account

interface AccountStorage {

    fun getAll(): List<Account>

    fun get(label: String): Account?

    fun add(account: Account)

    fun edit(account: Account)

    fun remove(account: Account)

}