package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ua.nanit.otpmanager.domain.otp.DigestAlgorithm
import ua.nanit.otpmanager.domain.time.SystemClock
import ua.nanit.otpmanager.domain.otp.TotpGenerator
import ua.nanit.otpmanager.domain.time.TotpListener
import ua.nanit.otpmanager.domain.time.TotpTimer

@Serializable
@SerialName("totp")
class TotpAccount(
    override val label: String,
    override var name: String,
    override val issuer: String?,
    override val secret: ByteArray,
    override val algorithm: DigestAlgorithm,
    override val digits: Int,
    val interval: Long
) : Account() {

    @Transient
    override var password: String = generate()

    @Transient
    var listener: TotpListener? = null
        set(value) {
            field = value
            value?.onTick(secondsRemain())
        }

    init {
        TotpTimer.subscribe(this)
    }

    protected fun finalize() {
        listener = null
        TotpTimer.unsubscribe(this)
    }

    fun update() {
        password = generate()
    }

    private fun generate(): String =
        TotpGenerator.INSTANCE.generate(secret, interval, algorithm, digits)

    fun secondsRemain(): Int {
        return (interval - SystemClock.epochSeconds() % interval).toInt()
    }
}