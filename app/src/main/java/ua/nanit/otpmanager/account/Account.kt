package ua.nanit.otpmanager.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.otp.Otp

@Serializable
sealed class Account {
    abstract val id: Int
    abstract val name: String
    abstract val secret: String

    abstract fun password(): String
}

@Serializable
@SerialName("totp")
data class TotpAccount(
    override val id: Int,
    override val name: String,
    override val secret: String,
    private val interval: Long = 30
) : Account() {

    override fun password(): String {
        return Otp.totp(secret, interval)
    }
}

@Serializable
@SerialName("hotp")
data class HotpAccount(
    override val id: Int,
    override val name: String,
    override val secret: String,
    private val counter: Long = 0
) : Account() {

    override fun password(): String {
        return Otp.hotp(secret, counter)
    }
}