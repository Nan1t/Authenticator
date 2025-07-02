package ua.nanit.otpmanager.domain.migration

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
) : AbstractMigration(repository) {

    companion object {
        private const val INNER_FILE_NAME = "accounts.bin"
        const val FILE_NAME = "accounts"
        const val FILE_EXT = "zip"
    }

    fun export(pin: String, fileUri: Uri): String {
        val params = getOtpParams()
        val payload = MigrationPayload(params)
        val bytes = ProtoBuf.encodeToByteArray(payload)
        val zipParams = ZipParameters().apply {
            isEncryptFiles = true
            encryptionMethod = EncryptionMethod.AES
            fileNameInZip = INNER_FILE_NAME
        }

        context.contentResolver.openOutputStream(fileUri)?.use { out ->
            ZipOutputStream(out, pin.toCharArray()).use { zipOs ->
                zipOs.putNextEntry(zipParams)
                zipOs.write(bytes, 0, bytes.size)
                zipOs.closeEntry()
            }
        }

        return "$FILE_NAME.$FILE_EXT"
    }

    suspend fun import(input: InputStream, pin: String): Int {
        return ZipInputStream(input, pin.toCharArray()).use { stream ->
            stream.nextEntry
            val data = stream.readBytes()
            val payload: MigrationPayload = ProtoBuf.decodeFromByteArray(data)
            val count = importPayload(payload)

            count
        }
    }

    interface FileSaver {
        fun save(name: String, extension: String, os: (OutputStream) -> Unit)
    }
}