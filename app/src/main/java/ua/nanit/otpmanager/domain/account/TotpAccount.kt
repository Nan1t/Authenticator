package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.domain.otp.TotpGenerator
import ua.nanit.otpmanager.domain.time.SystemClock

@Serializable
@SerialName("totp")
class TotpAccount(
    override val label: String,
    override val name: String,
    override val issuer: String?,
    override val secret: ByteArray,
    override val algorithm: String,
    override val digits: Int,
    val interval: Long
) : Account() {

    override var currentPassword: String = generate()

    fun secondsToUpdate(): Long {
        return interval - SystemClock.epochSeconds() % interval
    }

    override fun update() {
        currentPassword = generate()
    }

    private fun generate(): String =
        TotpGenerator.INSTANCE.generate(secret, interval, algorithm, digits)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as TotpAccount

        return label == other.label
                && secret.contentEquals(other.secret)
                && interval == other.interval
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + interval.hashCode()
        return result
    }
}