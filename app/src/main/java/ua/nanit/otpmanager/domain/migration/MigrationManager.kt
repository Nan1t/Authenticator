package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import ua.nanit.otpmanager.domain.account.AccountRepository
import ua.nanit.otpmanager.domain.encode.Base64Coder
import java.net.URI
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject
import kotlin.math.ceil

class MigrationManager @Inject constructor(
    private val storage: AccountRepository,
    private val base64Coder: Base64Coder
) {

    companion object {
        private const val VERSION = 1
        private const val BATCH_SIZE = 10
        private const val URI_SCHEMA = "otpauth-migration"
        private const val URI_HOST = "offline"
    }

    private val mapper = AccountMapper()
    private var cachedPayload: List<URI>? = null

    fun getPayload(index: Int): Payload? {
        var payload = cachedPayload

        if (payload == null) {
            payload = createPayloadUri()
            cachedPayload = payload
        }

        if (payload.isEmpty() || index < 0 || index >= payload.size)
            return null

        return Payload(payload[index], index+1, payload.size)
    }

    private fun createPayloadUri(): List<URI> {
        val payload = createPayload()

        if (payload.isEmpty())
            return emptyList()

        val uris = LinkedList<URI>()

        for (elem in payload) {
            val bytes = ProtoBuf.encodeToByteArray(elem)
            val encoded = base64Coder.encode(bytes)
            val data = URLEncoder.encode(encoded, "UTF-8")
            val uri = "$URI_SCHEMA://$URI_HOST?data=$data"
            uris.add(URI.create(uri))
        }

        return uris
    }

    private fun createPayload(): List<MigrationPayload> {
        val migrationAccounts = storage.getAll().map(mapper::mapToOtpParams)

        if (migrationAccounts.isEmpty())
            return emptyList()

        val result = LinkedList<MigrationPayload>()

        val batchId = migrationAccounts.hashCode()
        val batchSize = ceil(migrationAccounts.size.toFloat() / 10).toInt()
        var payload = LinkedList<OtpParams>()
        var batchIndex = 0
        var i = 1

        for (account in migrationAccounts) {
            if (i == BATCH_SIZE) {
                result.add(MigrationPayload(payload, VERSION, batchSize, batchIndex, batchId))
                payload = LinkedList()
                batchIndex++
                i = 1
                continue
            }

            payload.add(account)
            i++
        }

        if (payload.isNotEmpty())
            result.add(MigrationPayload(payload, VERSION, batchSize, batchIndex, batchId))

        return result
    }

}

data class Payload(
    val uri: URI,
    val page: Int,
    val pages: Int
)