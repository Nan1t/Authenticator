package ua.nanit.otpmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.domain.account.AccountManager
import javax.inject.Inject

@AndroidEntryPoint
class FileExportService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(job)

    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var manager: AccountManager

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService() ?: return
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, baseNotification().build())

        coroutineScope.launch(Dispatchers.IO) {
            val data = manager.export()

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                exportAccountsLegacy(data)
            } else {
                exportAccountsQ(data)
            }
        }

        return START_STICKY
    }

    private fun exportAccountsLegacy(data: String) {
        try {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = dir.resolve("accounts.json")

            if (!dir.exists())
                dir.mkdirs()

            if (!file.exists())
                file.createNewFile()

            file.writeText(data)
            stopWithNotification(true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            stopWithNotification(false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportAccountsQ(data: String) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "accounts")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/json")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            contentResolver.openOutputStream(uri)?.apply {
                write(data.toByteArray())
                close()
            }

            stopWithNotification(true)
        } else {
            stopWithNotification(false)
        }
    }

    private fun stopWithNotification(success: Boolean) {
        stopSelf()

        val builder = baseNotification()

        if (success) {
            builder.setContentTitle(getString(R.string.account_export_success))
            builder.setContentText(getString(R.string.account_export_success_desc))
            builder.setSmallIcon(R.drawable.ic_round_check)
        } else {
            builder.setContentTitle(getString(R.string.account_export_error))
            builder.setSmallIcon(R.drawable.ic_round_error_outline)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun baseNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentTitle(getString(R.string.account_export_process))
            .setSmallIcon(R.drawable.ic_round_import_export)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}