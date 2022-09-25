package ua.nanit.otpmanager.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.domain.account.AccountManager
import javax.inject.Inject

@AndroidEntryPoint
class AccountExportService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(job)

    private lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var manager: AccountManager

    override fun onCreate() {
        notificationManager = getSystemService() ?: return
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, baseNotification())

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
            updateNotification(R.string.account_export_success)
        } catch (ex: Exception) {
            ex.printStackTrace()
            updateNotification(R.string.account_export_error)
        } finally {
            stopSelf()
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

            updateNotification(R.string.account_export_success)
        } else {
            updateNotification(R.string.account_export_error)
        }

        stopSelf()
    }

    private fun updateNotification(@StringRes resId: Int) {
        val notification = NotificationCompat.Builder(this, "otpauth")
            .setContentTitle("Exporting 2FA accounts ...")
            .setContentText(getString(resId))
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

        println("Updates notify to ${getString(resId)}")
    }

    private fun baseNotification(): Notification {
        return NotificationCompat.Builder(this, "otpauth")
            .setContentTitle("Exporting 2FA accounts ...")
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}