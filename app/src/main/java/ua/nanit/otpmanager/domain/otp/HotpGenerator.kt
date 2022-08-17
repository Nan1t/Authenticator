package ua.nanit.otpmanager.domain.otp

import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.math.pow

open class HotpGenerator : OtpGenerator {

    override fun generate(secret: ByteArray, value: Long, algorithm: String, digits: Int): String {
        val hash = hash(algorithm, secret, value.toBytes())
        val offset = hash.last().and(0x0f).toInt()
        val truncated = truncate(hash, offset)
        val code = truncated % 10.0.pow(digits).toInt()
        return code.toString().padStart(digits, '0')
    }

    private fun hash(algorithm: String, key: ByteArray, value: ByteArray): ByteArray {
        return Mac.getInstance("Hmac$algorithm").run {
            init(SecretKeySpec(key, algorithm))
            doFinal(value)
        }
    }

    private fun truncate(hash: ByteArray, offset: Int): Int {
        return ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset+1].toInt() and 0xff) shl 16) or
                ((hash[offset+2].toInt() and 0xff) shl 8) or
                (hash[offset+3].toInt() and 0xff)
    }

    private fun Long.toBytes(): ByteArray = ByteBuffer.allocate(8)
        .putLong(this)
        .array()
}