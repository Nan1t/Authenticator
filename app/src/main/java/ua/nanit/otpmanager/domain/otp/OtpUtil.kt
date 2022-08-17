package ua.nanit.otpmanager.domain.otp

import java.lang.StringBuilder

const val MIN_DIGITS = 4
const val MAX_DIGITS = 9

private val splitIndices = mapOf(
    5 to intArrayOf(3),
    6 to intArrayOf(3),
    7 to intArrayOf(4),
    8 to intArrayOf(4),
    9 to intArrayOf(3, 7)
)

fun String.formatAsOtp(): String {
    if (this.length > MAX_DIGITS || this.length <= MIN_DIGITS)
        return this

    val indices = splitIndices[this.length]!!
    val result = StringBuilder()
    var last = 0

    for (i in indices) {
        result.append(this.substring(last, i))
        result.append(" ")
        last = i
    }

    result.append(this.substring(last, this.length))

    return result.toString()
}