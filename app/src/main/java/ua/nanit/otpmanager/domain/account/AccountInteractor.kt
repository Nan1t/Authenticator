package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.util.Base32
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountInteractor @Inject constructor(
    private val repository: AccountRepository,
    private val factory: AccountFactory,
    private val editor: AccountEditor
) {

    private var accounts: MutableList<Account>? = null

    suspend fun getAll(): List<Account> {
        if (accounts == null)
            accounts = repository.getAll().toMutableList()

        return accounts!!
    }

    suspend fun createTotpAccount(name: String, secret: String, interval: Long) {
        if (interval < 1) throw InvalidIntervalException()
        val key = Base32.decode(secret)
        validateAccount(name, key)
        val account = factory.createTotp(name, key, interval)
        accounts?.add(account)
    }

    suspend fun createHotpAccount(name: String, secret: String, counter: Long) {
        if (counter < 0) throw InvalidCounterException()

        val key = Base32.decode(secret)
        validateAccount(name, key)
        val account = factory.createHotp(name, key, counter)
        accounts?.add(account)
    }

    suspend fun updateAccount(account: Account) {
        if (account is HotpAccount) {
            editAccount(account)
        }
    }

    suspend fun editAccount(account: Account) {
        editor.editAccount(account)
    }

    suspend fun removeAccount(account: Account) {
        editor.removeAccount(account)
        accounts?.remove(account)
    }

    private fun validateAccount(name: String, secret: ByteArray) {
        if (name.length < 3)
            throw ShortNameException()

        if (secret.size < 6)
            throw ShortSecretException()
    }
}

class ShortNameException : RuntimeException()
class ShortSecretException : RuntimeException()

class InvalidIntervalException : RuntimeException()
class InvalidCounterException : RuntimeException()