package ua.nanit.otpmanager.domain

import ua.nanit.otpmanager.otp.Otp

interface Account {
    val name: String
    val secret: String

    fun code(): String
}

class TotpAccount(
    override val name: String,
    override val secret: String,
    val interval: Long = 30
) : Account {

    override fun code(): String {
        return Otp.totp(secret, interval)
    }
}

class HotpAccount(
    override val name: String,
    override val secret: String,
    val counter: Long
) : Account {

    override fun code(): String {
        return Otp.hotp(secret, counter)
    }
}