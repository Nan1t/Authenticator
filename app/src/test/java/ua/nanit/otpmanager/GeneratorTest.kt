package ua.nanit.otpmanager

import org.junit.Assert.*
import org.junit.Test
import ua.nanit.otpmanager.domain.Base32
import ua.nanit.otpmanager.domain.account.TotpAccount
import ua.nanit.otpmanager.domain.otp.TotpGenerator
import ua.nanit.otpmanager.domain.time.TotpTimer

class GeneratorTest {

    /**
     * Test cases: https://www.rfc-editor.org/rfc/rfc6238#appendix-B
     */
    @Test
    fun testTotpGen() {
        val secret = "12345678901234567890".toByteArray(Charsets.US_ASCII)
        val secret32 = "12345678901234567890123456789012".toByteArray(Charsets.US_ASCII)
        val secret64 = "1234567890123456789012345678901234567890123456789012345678901234".toByteArray(Charsets.US_ASCII)
        val interval = 30L
        val digits = 8

        assertEquals(totp(59).generate(secret, interval, "SHA1", digits), "94287082")
        assertEquals(totp(59).generate(secret32, interval, "SHA256", digits), "46119246")
        assertEquals(totp(59).generate(secret64, interval, "SHA512", digits), "90693936")

        assertEquals(totp(1111111109).generate(secret, interval, "SHA1", digits), "07081804")
        assertEquals(totp(1111111109).generate(secret32, interval, "SHA256", digits), "68084774")
        assertEquals(totp(1111111109).generate(secret64, interval, "SHA512", digits), "25091201")

        assertEquals(totp(1111111111).generate(secret, interval, "SHA1", digits), "14050471")
        assertEquals(totp(1111111111).generate(secret32, interval, "SHA256", digits), "67062674")
        assertEquals(totp(1111111111).generate(secret64, interval, "SHA512", digits), "99943326")

        assertEquals(totp(1234567890).generate(secret, interval, "SHA1", digits), "89005924")
        assertEquals(totp(1234567890).generate(secret32, interval, "SHA256", digits), "91819424")
        assertEquals(totp(1234567890).generate(secret64, interval, "SHA512", digits), "93441116")

        assertEquals(totp(2000000000).generate(secret, interval, "SHA1", digits), "69279037")
        assertEquals(totp(2000000000).generate(secret32, interval, "SHA256", digits), "90698825")
        assertEquals(totp(2000000000).generate(secret64, interval, "SHA512", digits), "38618901")

        assertEquals(totp(20000000000).generate(secret, interval, "SHA1", digits), "65353130")
        assertEquals(totp(20000000000).generate(secret32, interval, "SHA256", digits), "77737706")
        assertEquals(totp(20000000000).generate(secret64, interval, "SHA512", digits), "47863826")
    }

    // If TOTP generator works, assume HOTP does too

    @Test
    fun accountTest() {
        TotpTimer.start()

        val account = TotpAccount(
            "Test:test@account.com",
            "test@account.com",
            "Test",
            Base32.decode("I65VU7K5ZQL7WB4E"),
            "SHA1",
            6,
            30
        )

        TotpTimer.subscribe(0) {
            println("Progress ${account.progress(100)}")
            if (account.update()) {
                println("Updated ${account.password}")
            }
        }

        Thread.sleep(Long.MAX_VALUE)
    }

    private fun totp(time: Long) = TotpGenerator(ConstClock(time))
}