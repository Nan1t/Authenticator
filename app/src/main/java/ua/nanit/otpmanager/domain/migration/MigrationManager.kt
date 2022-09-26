package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import ua.nanit.otpmanager.domain.account.AccountRepository
import ua.nanit.otpmanager.domain.encode.Base64Coder
import java.net.URI
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

    fun createUris(): List<URI> {
        val payload = createPayload()
        val uris = LinkedList<URI>()

        for (elem in payload) {
            val bytes = ProtoBuf.encodeToByteArray(elem)
            val base64Str = base64Coder.encode(bytes)
            uris.add(URI("$URI_SCHEMA://$URI_HOST?data=$base64Str"))
        }

        return uris
    }

    private fun createPayload(): List<MigrationPayload> {
        val migrationAccounts = storage.getAll().map(mapper::mapToOtpParams)
        val result = LinkedList<MigrationPayload>()

        val batchSize = ceil(migrationAccounts.size.toFloat() / 10).toInt()
        var payload = LinkedList<OtpParams>()
        var batchIndex = 0
        var i = 1

        for (account in migrationAccounts) {
            if (i == BATCH_SIZE) {
                result.add(MigrationPayload(
                    0,
                    batchIndex,
                    batchSize,
                    payload,
                    VERSION
                ))
                payload = LinkedList()
                batchIndex++
                i = 1
                continue
            }

            payload.add(account)
            i++
        }

        if (payload.isNotEmpty())
            result.add(MigrationPayload(
                0,
                batchIndex,
                batchSize,
                payload,
                VERSION
            ))

        return result
    }

}