package ua.nanit.otpmanager.presentation.ext

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private const val MODE_KEY = "nightMode"

fun Context.settingsPreferences(): SharedPreferences {
    return getSharedPreferences("settings", Context.MODE_PRIVATE)
}

fun Context.isNightMode(): Boolean {
    return settingsPreferences().getBoolean(MODE_KEY, false)
}

fun Context.switchNightMode() {
    setNightMode(!isNightMode())
}

fun Context.updateNightMode() {
    setNightMode(isNightMode())
}

fun Context.setNightMode(isNightMode: Boolean) {
    settingsPreferences().edit()
        .putBoolean(MODE_KEY, isNightMode)
        .apply()

    val mode = if (isNightMode)
        AppCompatDelegate.MODE_NIGHT_YES
    else
        AppCompatDelegate.MODE_NIGHT_NO

    AppCompatDelegate.setDefaultNightMode(mode)
}