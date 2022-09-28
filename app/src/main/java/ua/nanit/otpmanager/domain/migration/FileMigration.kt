package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import net.lingala.zip4j.io.outputstream.ZipOutputStream
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import ua.nanit.otpmanager.domain.account.AccountRepository
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
    }

    fun export(pin: String): Boolean {
        val params = getOtpParams()
        val payload = MigrationPayload(params)
        val bytes = ProtoBuf.encodeToByteArray(payload)
        val zipParams = ZipParameters().apply {
            isEncryptFiles = true
            encryptionMethod = EncryptionMethod.AES
            fileNameInZip = INNER_FILE_NAME
        }

        return fileSaver.save("accounts", "zip") { fos ->
            ZipOutputStream(fos, pin.toCharArray()).use { zipOs ->
                zipOs.putNextEntry(zipParams)
                zipOs.write(bytes, 0, bytes.size)
                zipOs.closeEntry()
            }
        }
    }

    fun import(data: ByteArray, pin: String) {

    }

    interface FileSaver {
        fun save(name: String, extension: String, os: (OutputStream) -> Unit): Boolean
    }
}