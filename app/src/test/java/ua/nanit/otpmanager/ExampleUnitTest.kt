package ua.nanit.otpmanager

import org.junit.Test

import org.junit.Assert.*
import ua.nanit.otpmanager.totp.Totp

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        println(Totp.genPassword("I65VU7K5ZQL7WB4E", 30))
    }
}