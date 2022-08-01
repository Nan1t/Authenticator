package ua.nanit.otpmanager.totp

import org.apache.commons.codec.binary.Base32
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream
import java.io.IOException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Totp {

    fun genPassword(key: String, interval: Int): String {
        val counter = counter(interval).toByteArray()
        val hash = hmacSha1(Base32().decode(key), counter)
        val offset = hash[hash.size - 1].toInt() and 0xf
        val truncated = truncateHash(hash, offset)
        val code = truncated % 1000000
        return padOutput(code)
    }

    private fun truncateHash(hash: ByteArray, offset: Int): Int {
        return ((hash[offset].toInt() and 0x7f) shl 24) or
                ((hash[offset+1].toInt() and 0xff) shl 16) or
                ((hash[offset+2].toInt() and 0xff) shl 8) or
                (hash[offset+3].toInt() and 0xff)
    }

    private fun padOutput(value: Int): String {
        var result = value.toString()
        for (i in result.length until 6) {
            result = "0$result"
        }
        return result
    }

    private fun counter(interval: Int) = unixTime() / interval

    private fun unixTime() = System.currentTimeMillis() / 1000

    private fun hmacSha1(key: ByteArray, value: ByteArray): ByteArray {
        val mac: Mac = Mac.getInstance("HMACSHA1")
        mac.init(SecretKeySpec(key, "HMACSHA1"))
        return mac.doFinal(value)
    }
}

fun Long.toByteArray(): ByteArray {
    val buf = ByteArray(4)
    buf[0] = (this shr 0).toByte()
    buf[1] = (this shr 8).toByte()
    buf[2] = (this shr 16).toByte()
    buf[3] = (this shr 24).toByte()
    return buf
}