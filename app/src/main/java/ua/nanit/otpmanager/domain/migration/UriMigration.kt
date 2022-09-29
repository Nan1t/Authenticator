package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import ua.nanit.otpmanager.domain.UriParser
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
        private const val URI_SCHEME = "otpauth-migration"
        private const val URI_HOST = "offline"
    }

    private var payloadCache: List<URI>? = null

    suspend fun import(uri: String): ImportResult {
        val parsed = UriParser.parse(uri)

        if (parsed.uri.scheme != URI_SCHEME)
            throw IllegalArgumentException("Wrong URI scheme")

        if (parsed.uri.host != URI_HOST)
            throw IllegalArgumentException("Wrong URI host")

        val dataStr = parsed.args["data"]
            ?: throw IllegalArgumentException("Missing data argument")
        val payload = decodePayload(dataStr)

        if (payload.version > VERSION)
            throw IllegalArgumentException("Unsupported migration version")

        val count = importPayload(payload)
        val isLast = payload.batchIndex >= payload.batchSize - 1

        return ImportResult(count, isLast)
    }

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
            val data = encodePayload(elem)
            val uri = "${URI_SCHEME}://${URI_HOST}?data=$data"
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

    private fun decodePayload(data: String): MigrationPayload {
        val base64Decoded = base64Coder.decode(data)
        return ProtoBuf.decodeFromByteArray(base64Decoded)
    }

    private fun encodePayload(payload: MigrationPayload): String {
        val bytes = ProtoBuf.encodeToByteArray(payload)
        val base64Encoded = base64Coder.encode(bytes)
        return URLEncoder.encode(base64Encoded, Charsets.UTF_8.name())
    }

    class Payload(
        val uri: URI,
        val page: Int,
        val pages: Int
    )

    class ImportResult(
        val count: Int,
        val last: Boolean
    )
}