package ua.nanit.otpmanager

import ua.nanit.otpmanager.domain.encode.Base64Coder
import java.util.*

object JavaBase64Coder : Base64Coder {

    override fun encode(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }

    override fun decode(value: String): ByteArray {
        return Base64.getDecoder().decode(value)
    }

}