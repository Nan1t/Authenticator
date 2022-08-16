package ua.nanit.otpmanager.domain.otp

import java.lang.StringBuilder

fun String.formatAsOtp(): String {
    val result = StringBuilder()
    result.append(this.substring(0, 3))
    result.append(" ")
    result.append(this.substring(3, 6))
    return result.toString()
}