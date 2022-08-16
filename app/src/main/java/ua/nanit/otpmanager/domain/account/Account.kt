package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.domain.Clock
import ua.nanit.otpmanager.domain.otp.HotpGenerator
import ua.nanit.otpmanager.domain.otp.OtpGenerator
import ua.nanit.otpmanager.domain.otp.TotpGenerator

private val totp = TotpGenerator()
private val hotp = HotpGenerator()

@Serializable
sealed class Account {
    abstract val id: Int
    abstract val name: String
    abstract val secret: ByteArray
    abstract val generator: OtpGenerator
    abstract var currentPassword: String

    abstract fun update()
}

@Serializable
@SerialName("totp")
class TotpAccount(
    override val id: Int,
    override val name: String,
    override val secret: ByteArray,
    val interval: Long
) : Account() {

    override val generator: OtpGenerator = totp
    override var currentPassword: String =
        generator.generate(secret, interval)

    fun secondsToUpdate(): Long {
        return interval - Clock.epochSeconds() % interval
    }

    override fun update() {
        currentPassword = generator.generate(secret, interval)
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
    var counter: Long
) : Account() {

    override val generator: OtpGenerator = hotp
    override var currentPassword: String =
        generator.generate(secret, counter)

    override fun update() {
        counter += 1
        currentPassword = generator.generate(secret, counter)
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