package ua.nanit.otpmanager.domain.encode

interface Base64Coder {

    fun encode(bytes: ByteArray): String

    fun decode(value: String): ByteArray

}