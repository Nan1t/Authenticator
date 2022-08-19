package ua.nanit.otpmanager.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.util.Base32
import ua.nanit.otpmanager.util.UriParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountInteractor @Inject constructor(
    private val repository: AccountRepository,
    private val factory: AccountFactory,
    private val editor: AccountEditor
) {

    val accounts: Flow<List<Account>> = flow {
        emit(repository.getAll())
    }

    suspend fun createByUri(rawUri: String) {
        val uri = UriParser.parse(rawUri)
        val scheme = uri.uri.scheme.lowercase()

        if (scheme != "otpauth") throw InvalidUriSchemeException()

        val type = uri.uri.host.lowercase()
        val label = uri.uri.path.substring(1)
        val secret = uri.args["secret"] ?: ""
        val algorithm = uri.args["algorithm"] ?: Constants.DEFAULT_ALGORITHM
        val digits = uri.args["digits"]?.toInt() ?: Constants.DEFAULT_DIGITS

        return when (type) {
            "totp" -> {
                val period = uri.args["period"]?.toLong() ?: Constants.DEFAULT_TOTP_INTERVAL
                createTotpAccount(label, secret, algorithm, digits, period)
            }
            "hotp" -> {
                val counter = uri.args["counter"]?.toLong() ?: Constants.DEFAULT_HOTP_COUNTER
                createHotpAccount(label, secret, algorithm, digits, counter)
            }
            else -> throw IllegalArgumentException("Undefined key type: $type")
        }
    }

    suspend fun createTotpAccount(
        name: String,
        secret: String,
        algorithm: String,
        digits: Int,
        interval: Long
    ) {
        if (interval < 1) throw InvalidIntervalException()
        val key = Base32.decode(secret)
        validateAccount(name, key)
        val account = factory.createTotp(name, key, algorithm, digits, interval)
//        accounts?.add(account)
//        accountsFlow.emit(account)
    }

    suspend fun createHotpAccount(
        name: String,
        secret: String,
        algorithm: String,
        digits: Int,
        counter: Long
    ) {
        if (counter < 0) throw InvalidCounterException()
        val key = Base32.decode(secret)
        validateAccount(name, key)
        val account = factory.createHotp(name, key, algorithm, digits, counter)
//        accounts?.add(account)
//        accountsFlow.emit(account)
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
        //accounts?.remove(account)
    }

    private fun validateAccount(name: String, secret: ByteArray) {
        if (name.length < 3)
            throw ShortNameException()

        if (secret.size < 6)
            throw ShortSecretException()
    }
}

class InvalidUriSchemeException : RuntimeException()

class ShortNameException : RuntimeException()
class ShortSecretException : RuntimeException()

class InvalidIntervalException : RuntimeException()
class InvalidCounterException : RuntimeException()