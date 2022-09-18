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

    override var password: String = generate()

    fun increment() {
        counter += 1
        password = generate()
    }

    private fun generate(): String =
        HotpGenerator.INSTANCE.generate(secret, counter, algorithm, digits)

}