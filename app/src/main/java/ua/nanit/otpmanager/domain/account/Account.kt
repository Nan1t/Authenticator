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

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as TotpAccount

        return id == other.id
                && name == other.name
                && secret.contentEquals(other.secret)
                && interval == other.interval
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + interval.hashCode()
        return result
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

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as HotpAccount

        return id == other.id
                && name == other.name
                && secret.contentEquals(other.secret)
                && counter == other.counter
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + counter.hashCode()
        return result
    }
}