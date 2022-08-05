package ua.nanit.otpmanager.domain.account

import kotlinx.coroutines.flow.MutableSharedFlow
import ua.nanit.otpmanager.util.Base32

class AccountInteractor(
    private val repository: AccountRepository,
    private val factory: AccountFactory,
    private val editor: AccountEditor
) {

    private var accountsFlow: MutableSharedFlow<Account>? = null

//    suspend fun getAllAccounts(): SharedFlow<Account> {
//        var flow = accountsFlow
//
//        if (flow == null)
//            flow = repository.getAll().asFlow()
//
//        return flow
//    }

    suspend fun createTotpAccount(name: String, secret: String, interval: Long) {
        if (interval < 1) throw InvalidIntervalException()
        val key = Base32.decode(secret)
        validateAccount(name, key)
        val account = factory.createTotp(name, key, interval)
        accountsFlow?.emit(account)
    }

    suspend fun createHotpAccount(name: String, secret: String, counter: Long) {
        if (counter < 0) throw InvalidCounterException()

        val key = Base32.decode(secret)
        validateAccount(name, key)
        val account = factory.createHotp(name, key, counter)
        accountsFlow?.emit(account)
    }

    suspend fun editAccount(account: Account) {
        editor.editAccount(account)
    }

    suspend fun removeAccount(account: Account) {
        editor.removeAccount(account)
    }

    private fun validateAccount(name: String, secret: ByteArray) {
        if (name.length < 3)
            throw ShortNameException()

        if (secret.size < 16)
            throw ShortSecretException()
    }
}

class ShortNameException : RuntimeException()
class ShortSecretException : RuntimeException()

class InvalidIntervalException : RuntimeException()
class InvalidCounterException : RuntimeException()