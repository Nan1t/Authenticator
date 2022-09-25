package ua.nanit.otpmanager.presentation.export

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar
import ua.nanit.otpmanager.service.AccountExportService

class AccountExporter(private val fragment: Fragment) {

    private val ctx: Context = fragment.requireContext()
    private val writePermLauncher: ActivityResultLauncher<String>

    init {
        writePermLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                export()
            } else {
                fragment.showCloseableSnackbar(R.string.account_export_permission)
            }
        }
    }

    fun export() {
        if (isPermsGranted()) {
            ctx.startService(Intent(ctx, AccountExportService::class.java))
        } else {
            writePermLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun isPermsGranted(): Boolean =
        ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED
}