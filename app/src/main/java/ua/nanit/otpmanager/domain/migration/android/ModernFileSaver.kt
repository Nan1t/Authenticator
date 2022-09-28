package ua.nanit.otpmanager.domain.migration.android

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import ua.nanit.otpmanager.domain.migration.FileMigration
import java.io.OutputStream

/**
 * Save file into Downloads folder on Android Q+
 */
class ModernFileSaver(private val ctx: Context) : FileMigration.FileSaver {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun save(name: String, extension: String, os: (OutputStream) -> Unit): Boolean {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/$extension")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = ctx.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        return if (uri != null) {
            ctx.contentResolver.openOutputStream(uri)?.use {
                os(it)
            }
            true
        } else {
            false
        }
    }
}