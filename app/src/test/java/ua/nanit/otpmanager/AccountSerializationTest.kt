package ua.nanit.otpmanager

import kotlinx.serialization.decodeFromString
import org.junit.Test
import org.junit.Assert.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.nanit.otpmanager.account.*

class AccountSerializationTest {

    @Test
    fun testSerialization() {
        val totp: Account = TotpAccount(777, "TotpAccount", "I65VU7K5ZQL7WB4E")
        val hotp: Account = HotpAccount(777, "HotpAccount", "I65VU7K5ZQ")

        val expectedTotp = "{\"type\":\"totp\",\"id\":777,\"name\":\"TotpAccount\",\"secret\":\"I65VU7K5ZQL7WB4E\"}"
        val expectedHotp = "{\"type\":\"hotp\",\"id\":777,\"name\":\"HotpAccount\",\"secret\":\"I65VU7K5ZQ\"}"

        assertEquals(expectedTotp, Json.encodeToString(totp))
        assertEquals(expectedHotp, Json.encodeToString(hotp))
    }

    @Test
    fun testDeserialization() {
        val jsonTotp = "{\"type\":\"totp\",\"id\":777,\"name\":\"TotpAccount\",\"secret\":\"I65VU7K5ZQL7WB4E\"}"
        val jsonHotp = "{\"type\":\"hotp\",\"id\":777,\"name\":\"HotpAccount\",\"secret\":\"I65VU7K5ZQ\"}"

        val totp = Json.decodeFromString<Account>(jsonTotp)
        val hotp = Json.decodeFromString<Account>(jsonHotp)

        assertEquals(TotpAccount(777, "TotpAccount", "I65VU7K5ZQL7WB4E"), totp)
        assertEquals(HotpAccount(777, "HotpAccount", "I65VU7K5ZQ"), hotp)
    }

}