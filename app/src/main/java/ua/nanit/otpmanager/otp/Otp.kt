package ua.nanit.otpmanager.otp

import org.apache.commons.codec.binary.Base32
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.math.floor

object Otp {

    private const val algorithm = "HmacSHA1"
    private val base32 = Base32()
    private val divider = arrayOf(0, 10, 100, 1000, 10000, 100000,
        1000000, 10000000, 100000000, 1000000000)

    fun totp(secret: String, interval: Long, digits: Int = 6): String {
        return hotp(secret, counter(interval), digits)
    }

    fun hotp(secret: String, counter: Long, digits: Int = 6): String {
        val key = base32.decode(secret)
        val hash = hmacSha1(key, counter.toBytes())
        val offset = hash.last().and(0x0f).toInt()
        val truncated = truncate(hash, offset)
        val code = truncated % divider[digits]
        return code.toString().padStart(digits, '0')
    }

    private fun truncate(hash: ByteArray, offset: Int): Int {
        return ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset+1].toInt() and 0xff) shl 16) or
                ((hash[offset+2].toInt() and 0xff) shl 8) or
                (hash[offset+3].toInt() and 0xff)
    }

    private fun hmacSha1(key: ByteArray, value: ByteArray): ByteArray {
        return Mac.getInstance(algorithm).run {
            init(SecretKeySpec(key, algorithm))
            doFinal(value)
        }
    }

    private fun counter(interval: Long) = floor(unixTime().toDouble() / interval).toLong()

    private fun unixTime() = System.currentTimeMillis() / 1000
}

fun Long.toBytes(): ByteArray = ByteBuffer.allocate(8)
    .putLong(this)
    .array()