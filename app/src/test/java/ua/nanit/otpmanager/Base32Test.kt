package ua.nanit.otpmanager

import org.junit.Test
import ua.nanit.otpmanager.util.Base32

class Base32Test {

    @Test
    fun test() {
        println(Base32.decode("RSDTFV&N*#N@F#").joinToString(","))
    }

}