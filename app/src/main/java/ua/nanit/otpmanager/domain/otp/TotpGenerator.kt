package ua.nanit.otpmanager.domain.otp

import ua.nanit.otpmanager.domain.Clock
import kotlin.math.floor

class TotpGenerator : HotpGenerator() {

    override fun generate(secret: ByteArray, value: Long, algorithm: String, digits: Int): String {
        return super.generate(secret, counter(value), algorithm, digits)
    }

    private fun counter(interval: Long) =
        floor(Clock.epochSeconds().toDouble() / interval).toLong()

}