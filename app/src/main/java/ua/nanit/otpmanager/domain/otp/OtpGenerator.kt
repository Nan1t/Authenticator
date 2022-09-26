package ua.nanit.otpmanager.domain.otp

interface OtpGenerator {

    fun generate(secret: ByteArray, value: Long, algorithm: DigestAlgorithm, digits: Int): String

}