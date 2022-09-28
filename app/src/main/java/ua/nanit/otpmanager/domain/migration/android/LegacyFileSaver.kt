package ua.nanit.otpmanager.domain.migration.android

import android.os.Environment
import ua.nanit.otpmanager.domain.migration.FileMigration
import java.io.*

/**
 * Save file into Downloads folder on Android before Q
 */
class LegacyFileSaver : FileMigration.FileSaver {

    override fun save(name: String, extension: String, os: (OutputStream) -> Unit) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = dir.resolve("$name.$extension")

        if (!dir.exists())
            dir.mkdirs()

        if (!file.exists())
            file.createNewFile()

        FileOutputStream(file).use(os)
    }
}