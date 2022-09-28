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

/**
 * Export/Import accounts using URI that contains Protobuf-encoded accounts
 */
class UriMigration @Inject constructor(
    repository: AccountRepository,
    private val base64Coder: Base64Coder
) : AbstractMigration(repository) {

    companion object {
        private const val VERSION = 1
        private const val BATCH_SIZE = 10
        private const val URI_SCHEMA = "otpauth-migration"
        private const val URI_HOST = "offline"
    }

    private var payloadCache: List<URI>? = null

    fun export(index: Int): Payload? {
        var payload = payloadCache

        if (payload == null) {
            payload = createPayloadUri()
            payloadCache = payload
        }

        if (payload.isEmpty() || index < 0 || index >= payload.size)
            return null

        return Payload(payload[index], index+1, payload.size)
    }

    private fun createPayloadUri(): List<URI> {
        val payload = createPayloadBatch()

        if (payload.isEmpty())
            return emptyList()

        val uris = LinkedList<URI>()

        for (elem in payload) {
            val bytes = ProtoBuf.encodeToByteArray(elem)
            val encoded = base64Coder.encode(bytes)
            val data = URLEncoder.encode(encoded, "UTF-8")
            val uri = "${URI_SCHEMA}://${URI_HOST}?data=$data"
            uris.add(URI.create(uri))
        }

        return uris
    }

    private fun createPayloadBatch(): List<MigrationPayload> {
        val otpParams = getOtpParams()

        if (otpParams.isEmpty())
            return emptyList()

        val result = LinkedList<MigrationPayload>()

        val batchId = otpParams.hashCode()
        val batchSize = ceil(otpParams.size.toFloat() / 10).toInt()
        var payload = LinkedList<OtpParams>()
        var batchIndex = 0
        var i = 1

        for (account in otpParams) {
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

    class Payload(
        val uri: URI,
        val page: Int,
        val pages: Int
    )
}