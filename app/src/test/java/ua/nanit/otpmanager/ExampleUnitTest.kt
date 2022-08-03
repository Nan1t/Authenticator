package ua.nanit.otpmanager

import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val secret = "I65VU7K5ZQL7WB4E"
        val interval = 30

        while (true) {
            println(Totp.generate(secret, interval))
            Thread.sleep(1000)
        }
    }
}