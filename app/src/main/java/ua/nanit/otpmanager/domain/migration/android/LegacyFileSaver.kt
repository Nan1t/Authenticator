package ua.nanit.otpmanager.domain.migration.android

import android.os.Environment
import ua.nanit.otpmanager.domain.migration.FileMigration

/**
 * Save file into Downloads folder on Android before Q
 */
class LegacyFileSaver : FileMigration.FileSaver {

    override fun save(data: ByteArray, name: String, extension: String): Boolean {
        return try {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = dir.resolve("$name.$extension")

            if (!dir.exists())
                dir.mkdirs()

            if (!file.exists())
                file.createNewFile()

            file.writeBytes(data)
            true
        } catch (ex: Exception) {
            false
        }
    }
}