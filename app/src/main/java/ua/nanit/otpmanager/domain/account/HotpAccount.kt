package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.domain.otp.HotpGenerator

@Serializable
@SerialName("hotp")
class HotpAccount(
    override val label: String,
    override val name: String,
    override val issuer: String?,
    override val secret: ByteArray,
    override val algorithm: String,
    override val digits: Int,
    var counter: Long
) : Account() {

    override var currentPassword: String = generate()

    override fun update() {
        counter += 1
        currentPassword = generate()
    }

    private fun generate(): String =
        HotpGenerator.INSTANCE.generate(secret, counter, algorithm, digits)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as HotpAccount

        return label == other.label
                && secret.contentEquals(other.secret)
                && counter == other.counter
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + counter.hashCode()
        return result
    }


}