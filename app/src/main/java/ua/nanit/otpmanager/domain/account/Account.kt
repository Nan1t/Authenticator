package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.domain.Otp

@Serializable
sealed class Account {
    abstract val id: Int
    abstract val name: String
    abstract val secret: ByteArray

    abstract fun password(): String
}

@Serializable
@SerialName("totp")
class TotpAccount(
    override val id: Int,
    override val name: String,
    override val secret: ByteArray,
    val interval: Long
) : Account() {

    override fun password(): String {
        return Otp.totp(secret, interval)
    }
}

@Serializable
@SerialName("hotp")
class HotpAccount(
    override val id: Int,
    override val name: String,
    override val secret: ByteArray,
    val counter: Long
) : Account() {

    override fun password(): String {
        return Otp.hotp(secret, counter)
    }
}