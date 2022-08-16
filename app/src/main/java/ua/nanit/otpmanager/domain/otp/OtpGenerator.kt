package ua.nanit.otpmanager.domain.otp

interface OtpGenerator {

    fun generate(secret: ByteArray, value: Long, digits: Int = 6): String

}