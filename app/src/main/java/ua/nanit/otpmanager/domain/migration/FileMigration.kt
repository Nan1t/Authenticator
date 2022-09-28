package ua.nanit.otpmanager.domain.migration

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import net.lingala.zip4j.io.inputstream.ZipInputStream
import net.lingala.zip4j.io.outputstream.ZipOutputStream
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import ua.nanit.otpmanager.domain.account.AccountRepository
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * Export/Import accounts with file, wrapped in password protected ZIP archive
 */
class FileMigration @Inject constructor(
    repository: AccountRepository,
    private val fileSaver: FileSaver
) : AbstractMigration(repository) {

    companion object {
        private const val INNER_FILE_NAME = "accounts.bin"
        private const val FILE_NAME = "accounts"
        private const val FILE_EXT = "zip"
    }

    fun export(pin: String): String {
        val params = getOtpParams()
        val payload = MigrationPayload(params)
        val bytes = ProtoBuf.encodeToByteArray(payload)
        val zipParams = ZipParameters().apply {
            isEncryptFiles = true
            encryptionMethod = EncryptionMethod.AES
            fileNameInZip = INNER_FILE_NAME
        }

        fileSaver.save(FILE_NAME, FILE_EXT) { fos ->
            ZipOutputStream(fos, pin.toCharArray()).use { zipOs ->
                zipOs.putNextEntry(zipParams)
                zipOs.write(bytes, 0, bytes.size)
                zipOs.closeEntry()
            }
        }

        return "$FILE_NAME.$FILE_EXT"
    }

    suspend fun import(input: InputStream, pin: String): Int {
        return withContext(Dispatchers.IO) {
            ZipInputStream(input, pin.toCharArray()).use { stream ->
                val entry = stream.nextEntry
                val data = ByteArray(entry.uncompressedSize.toInt())
                stream.read(data)
                val payload: MigrationPayload = ProtoBuf.decodeFromByteArray(data)
                importPayload(payload)

                payload.otpParameters.size
            }
        }
    }

    interface FileSaver {
        fun save(name: String, extension: String, os: (OutputStream) -> Unit)
    }
}