package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.domain.Base32
import ua.nanit.otpmanager.domain.UriParser
import ua.nanit.otpmanager.domain.storage.AccountStorage
import java.util.regex.Pattern
import javax.inject.Inject

class AccountManager @Inject constructor(
    private val repository: AccountRepository,
    private val storage: AccountStorage
) {

    private val labelPattern = Pattern.compile(":")

    suspend fun createByUri(rawUri: String): Account {
        val uri = UriParser.parse(rawUri)
        val scheme = uri.uri.scheme.lowercase()

        if (scheme != "otpauth")
            throw throw CreationError.INVALID_URI.asException()

        val type = uri.uri.host.lowercase()
        val label = uri.uri.path.substring(1)
        val secret = uri.args["secret"] ?: ""
        val issuer = uri.args["issuer"]
        val algorithm = uri.args["algorithm"] ?: Constants.DEFAULT_ALGORITHM
        val digits = uri.args["digits"]?.toInt() ?: Constants.DEFAULT_DIGITS

        return when (type) {
            "totp" -> {
                val period = uri.args["period"]?.toLong() ?: Constants.DEFAULT_TOTP_INTERVAL
                createTotpAccount(label, issuer, secret, algorithm, digits, period)
            }
            "hotp" -> {
                val counter = uri.args["counter"]?.toLong() ?: Constants.DEFAULT_HOTP_COUNTER
                createHotpAccount(label, issuer, secret, algorithm, digits, counter)
            }
            else -> throw CreationError.UNDEFINED_TYPE.asException()
        }
    }

    suspend fun createTotpAccount(
        label: String,
        issuer: String?,
        secret: String,
        algorithm: String,
        digits: Int,
        interval: Long
    ): Account {
        if (interval < 1) throw CreationError.INVALID_INTERVAL.asException()
        val key = Base32.decode(secret)
        validateAccount(label, key)
        val parsed = parseLabel(label)
        val account = TotpAccount(label, parsed.name, issuer ?: parsed.issuer,
            key, algorithm, digits, interval)
        repository.add(account)
        return account
    }

    suspend fun createHotpAccount(
        label: String,
        issuer: String?,
        secret: String,
        algorithm: String,
        digits: Int,
        counter: Long
    ): Account {
        if (counter < 0) throw CreationError.INVALID_COUNTER.asException()
        val key = Base32.decode(secret)
        validateAccount(label, key)
        val parsed = parseLabel(label)
        val account = HotpAccount(label, parsed.name, issuer ?: parsed.issuer,
            key, algorithm, digits, counter)
        repository.add(account)
        return account
    }

    fun export(): String {
        return storage.export()
    }

    private fun parseLabel(label: String): ParsedLabel {
        val arr = label.split(labelPattern, 2)
        val name = arr[0]
        val issuer = if (arr.size == 2) arr[1] else null
        return ParsedLabel(name, issuer)
    }

    private fun validateAccount(label: String, secret: ByteArray) {
        if (repository.get(label) != null)
            throw CreationError.ALREADY_EXISTS.asException()

        if (label.length < 3)
            throw CreationError.SHORT_LABEL.asException()

        if (secret.size < 6)
            throw CreationError.SHORT_SECRET.asException()
    }
}

private class ParsedLabel(val name: String, val issuer: String?)