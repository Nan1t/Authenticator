package ua.nanit.otpmanager.totp

import org.apache.commons.codec.binary.Base32
import java.nio.ByteBuffer
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.math.floor
import kotlin.math.pow

object Totp {

    private val base32 = Base32()
    private var digits = 6

    fun genPassword(key: String, interval: Int): String {
        val counter = ByteBuffer.allocate(8)
            .putLong(counter(interval))
            .array()
        val hash = hmacSha1(base32.decode(key), counter)
        val offset = hash.last().and(0x0f).toInt()
        val truncated = truncateHash(hash, offset)
        val code = truncated % 10.0.pow(digits).toInt()
        return code.toString().padStart(digits, '0')
    }

    private fun truncateHash(hash: ByteArray, offset: Int): Int {
        return ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset+1].toInt() and 0xff) shl 16) or
                ((hash[offset+2].toInt() and 0xff) shl 8) or
                (hash[offset+3].toInt() and 0xff)
    }

    private fun counter(interval: Int) = floor(unixTime().toDouble() / interval).toLong()

    private fun unixTime() = System.currentTimeMillis() / 1000

    private fun hmacSha1(key: ByteArray, value: ByteArray): ByteArray {
        val mac: Mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key, "RAW"))
        return mac.doFinal(value)
    }
}