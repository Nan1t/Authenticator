package ua.nanit.otpmanager.domain.otp

import ua.nanit.otpmanager.domain.time.Clock
import ua.nanit.otpmanager.domain.time.SystemClock
import kotlin.math.floor

class TotpGenerator(
    private val clock: Clock = SystemClock
) : HotpGenerator() {

    override fun generate(secret: ByteArray, value: Long, algorithm: String, digits: Int): String {
        return super.generate(secret, counter(value), algorithm, digits)
    }

    private fun counter(interval: Long) =
        floor(clock.epochSeconds().toDouble() / interval).toLong()

    companion object {
        val INSTANCE = TotpGenerator()
    }
}