package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.domain.encode.Base32
import ua.nanit.otpmanager.domain.UriParser
import ua.nanit.otpmanager.domain.otp.DigestAlgorithm
import javax.inject.Inject

class AccountManager @Inject constructor(
    private val repository: AccountRepository,
) {

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
        val parsed = LabelParser.parse(label)
        val digestAlgorithm = DigestAlgorithm.fromValue(algorithm)
            ?: throw CreationError.UNSUPPORTED_ALGORITHM.asException()
        val account = TotpAccount(label, parsed.name, issuer ?: parsed.issuer,
            key, digestAlgorithm, digits, interval)
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
        val parsed = LabelParser.parse(label)
        val digestAlgorithm = DigestAlgorithm.fromValue(algorithm)
            ?: throw CreationError.UNSUPPORTED_ALGORITHM.asException()
        val account = HotpAccount(label, parsed.name, issuer ?: parsed.issuer,
            key, digestAlgorithm, digits, counter)
        repository.add(account)
        return account
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