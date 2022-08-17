package ua.nanit.otpmanager

import org.junit.Test
import ua.nanit.otpmanager.domain.otp.formatAsOtp
import java.net.URI

class UriTest {

    @Test
    fun test() {
        println("0000".formatAsOtp())
        println("00000".formatAsOtp())
        println("000000".formatAsOtp())
        println("0000000".formatAsOtp())
        println("00000000".formatAsOtp())
        println("000000000".formatAsOtp())
    }

}