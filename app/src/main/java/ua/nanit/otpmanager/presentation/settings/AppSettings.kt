package ua.nanit.otpmanager.presentation.settings

import android.content.Context
import androidx.preference.PreferenceManager

class AppSettings(ctx: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)

    fun isNightMode(): Boolean {
        return prefs.getBoolean("nightMode", false)
    }

    fun isProtectAccounts(): Boolean {
        return prefs.getBoolean("securityMode", false)
    }

    fun getLocale(): String? {
        return prefs.getString("locale", null)
    }
}