package ua.nanit.otpmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.domain.migration.FileMigration
import javax.inject.Inject

@AndroidEntryPoint
class FileExportService : Service() {

    companion object {
        const val EXTRA_PIN_CODE = "pinCode"
        private const val NOTIFICATION_ID = 1
    }

    private val coroutineScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var migration: FileMigration

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pinCode = intent?.getStringExtra(EXTRA_PIN_CODE)
            ?: throw IllegalArgumentException("Missing pin code extra")

        startForeground(NOTIFICATION_ID, baseNotification().build())

        coroutineScope.launch(Dispatchers.IO) {
            try {
                val success = migration.export(pinCode)
                stopWithNotification(success)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun stopWithNotification(success: Boolean) {
        val notificationManager = getSystemService<NotificationManager>() ?: return
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
        stopSelf()
    }

    private fun baseNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setContentTitle(getString(R.string.account_export_process))
            .setSmallIcon(R.drawable.ic_round_import_export)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}