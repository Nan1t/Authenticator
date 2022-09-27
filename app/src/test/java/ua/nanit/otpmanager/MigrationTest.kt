package ua.nanit.otpmanager

import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.schema.ProtoBufSchemaGenerator
import org.junit.Test
import ua.nanit.otpmanager.domain.migration.MigrationPayload
import java.net.URLDecoder

class MigrationTest {

    private val base64 = JavaBase64Coder

    @Test
    fun test() {
        val data = URLDecoder.decode("CiIKCqM1xRD13HmXORQSDkdpdEh1YkBOYWl0TWl4IAEoATACChwKCnJV75M9ygFKRxcSCFNwaWdvdE1DIAEoATACCjgKCgDy31GJ5jzhx3sSG0Rpc2NvcmQ6bWF4aW0wMDk4QGdtYWlsLmNvbRoHRGlzY29yZCABKAEwAgpXCgrToybG5t1AesM3EkNCdWtraXQg0L%2FQvi3RgNGD0YHRgdC60LggLSDRgdCy0L7QuSDRgdC10YDQstC10YAgTWluZWNyYWZ0OiBfTmFuaXRfIAEoATACCj8KFM8iZ%2BbNB17yg4k7a2QqJDyF1mdIEhlHb29nbGU6bmFuaXRtaXhAZ21haWwuY29tGgZHb29nbGUgASgBMAIKHAoKy6JOjQoWMDVILBIITWNNYXJrZXQgASgBMAIKNgoUt11eeOTOwNHTx7ikfOdT7f%2FEJPMSEEhlcm9rdTpOYW5pdCBNaXgaBkhlcm9rdSABKAEwAgpAChThcsijb8jPI6%2B7ZzBzVkuS5D4UDxIaR29vZ2xlOm1heGltMDA5OEBnbWFpbC5jb20aBkdvb2dsZSABKAEwAgo0Cgq%2BFUlZBROMeV5hEhhQYXlQYWw6aW1uYW5pdEBnbWFpbC5jb20aBlBheVBhbCABKAEwAgpKChTAsYXr3cbZYMHyyMwN8f161UUm%2FhIeU2VsbGVyT25saW5lOmltbmFuaXRAZ21haWwuY29tGgxTZWxsZXJPbmxpbmUgASgBMAIQARgCIAAo5Pmbg%2Fj%2F%2F%2F%2F%2FAQ%3D%3D", "UTF-8")
        val bytes = base64.decode(data)
        val payload: MigrationPayload = ProtoBuf.decodeFromByteArray(bytes)

        println(payload)
    }

}