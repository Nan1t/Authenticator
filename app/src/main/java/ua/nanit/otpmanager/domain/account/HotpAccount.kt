package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ua.nanit.otpmanager.domain.otp.DigestAlgorithm
import ua.nanit.otpmanager.domain.otp.HotpGenerator

@Serializable
@SerialName("hotp")
class HotpAccount(
    override val label: String,
    override var name: String,
    override val issuer: String?,
    override val secret: ByteArray,
    override val algorithm: DigestAlgorithm,
    override val digits: Int,
    var counter: Long
) : Account() {

    companion object {
        private const val UPDATE_COOLDOWN = 3000
    }

    @Transient
    override var password: String = generate()

    @Transient
    private var lastUpdate: Long = 0

    fun increment(): Boolean {
        if (isReadyForUpdate()) {
            counter += 1
            password = generate()
            lastUpdate = System.currentTimeMillis()
            return true
        }
        return false
    }

    private fun generate(): String {
        return HotpGenerator.INSTANCE.generate(secret, counter, algorithm, digits)
    }

    private fun isReadyForUpdate(): Boolean {
        return System.currentTimeMillis() > lastUpdate + UPDATE_COOLDOWN
    }

}