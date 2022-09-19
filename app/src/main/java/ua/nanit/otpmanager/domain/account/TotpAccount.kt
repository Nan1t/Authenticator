package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ua.nanit.otpmanager.domain.time.SystemClock
import ua.nanit.otpmanager.domain.otp.TotpGenerator
import ua.nanit.otpmanager.domain.time.TotpListener
import ua.nanit.otpmanager.domain.time.TotpTimer

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

    @Transient
    override var password: String = generate()

    @Transient
    var listener: TotpListener? = null
        private set

    init {
        TotpTimer.subscribe(this)
    }

    fun listen(listener: TotpListener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    fun update() {
        password = generate()
    }

    fun secondsRemain(): Int {
        return (interval - SystemClock.epochSeconds() % interval).toInt()
    }

    private fun generate(): String =
        TotpGenerator.INSTANCE.generate(secret, interval, algorithm, digits)
}