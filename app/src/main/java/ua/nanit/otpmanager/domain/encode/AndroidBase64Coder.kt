package ua.nanit.otpmanager.domain.encode

import android.util.Base64

object AndroidBase64Coder : Base64Coder {

    override fun encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun decode(value: String): ByteArray {
        return Base64.decode(value, Base64.DEFAULT)
    }

}