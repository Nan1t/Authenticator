package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.domain.Clock
import ua.nanit.otpmanager.domain.otp.TotpGenerator

@Serializable
@SerialName("totp")
class TotpAccount(
    override val id: Int,
    override val name: String,
    override val secret: ByteArray,
    override val algorithm: String,
    override val digits: Int,
    val interval: Long
) : Account() {

    companion object {
        private val generator = TotpGenerator()
    }

    override var currentPassword: String = generate()

    fun secondsToUpdate(): Long {
        return interval - Clock.epochSeconds() % interval
    }

    override fun update() {
        currentPassword = generate()
    }

    private fun generate(): String = generator.generate(secret, interval, algorithm, digits)

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