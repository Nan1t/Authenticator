package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.nanit.otpmanager.domain.time.SystemClock
import ua.nanit.otpmanager.domain.otp.TotpGenerator

@Serializable
@SerialName("totp")
class TotpAccount(
    override val label: String,
    override val name: String,
    override val issuer: String?,
    override val secret: ByteArray,
    override val algorithm: String,
    override val digits: Int,
    private val interval: Long
) : Account() {

    override var password: String = generate()
    private var lastUpdate: Long = 0

    fun progress(multiplier: Int): Int = (secondsRemain() * multiplier / interval).toInt()

    // Problem here
    fun update(): Boolean {
        val seconds = SystemClock.epochSeconds()
        if (seconds > lastUpdate + interval) {
            password = generate()
            lastUpdate = seconds
            return true
        }
        return false
    }

    private fun secondsRemain(): Long {
        return interval - SystemClock.epochSeconds() % interval
    }

    private fun generate(): String =
        TotpGenerator.INSTANCE.generate(secret, interval, algorithm, digits)
}