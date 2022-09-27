package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import net.lingala.zip4j.io.outputstream.ZipOutputStream
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import ua.nanit.otpmanager.domain.account.AccountRepository
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/**
 * Export/Import accounts with file, wrapped in password protected ZIP archive
 */
class FileMigration @Inject constructor(
    repository: AccountRepository,
    private val fileSaver: FileSaver
) : AbstractMigration(repository) {

    companion object {
        private const val INNER_FILE_NAME = "accounts"
    }

    fun export(pin: String): Boolean {
        val params = getOtpParams()
        val payload = MigrationPayload(params)
        val bytes = ProtoBuf.encodeToByteArray(payload)

        val zipParams = ZipParameters().apply {
            isEncryptFiles = true
            encryptionMethod = EncryptionMethod.AES
            fileNameInZip = INNER_FILE_NAME
            entrySize = bytes.size.toLong()
        }
        val stream = ByteArrayOutputStream()
        val zipOs = ZipOutputStream(stream, pin.toCharArray())

        zipOs.putNextEntry(zipParams)
        zipOs.write(bytes)
        zipOs.closeEntry()

        val result = stream.toByteArray()

        zipOs.close()

        return fileSaver.save(result, "accounts", "zip")
    }

    fun import(data: ByteArray, pin: String) {

    }

    interface FileSaver {
        fun save(data: ByteArray, name: String, extension: String): Boolean
    }
}