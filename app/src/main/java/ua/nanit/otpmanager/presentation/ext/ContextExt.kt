package ua.nanit.otpmanager.presentation.ext

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import ua.nanit.otpmanager.presentation.settings.AppSettings
import java.util.*

fun Context.appSettings(): AppSettings = AppSettings(this)

fun Context.updateNightMode(isNightMode: Boolean = appSettings().isNightMode()) {
    val mode = if (isNightMode)
        AppCompatDelegate.MODE_NIGHT_YES
    else
        AppCompatDelegate.MODE_NIGHT_NO

    AppCompatDelegate.setDefaultNightMode(mode)
}

fun Context.updateLocale() {
    appSettings().getLocale()?.let { setLocale(it) }
}

fun Context.setLocale(language: String) {
    val config = resources.configuration
    val locale = Locale(language)

    Locale.setDefault(locale)
    config.setLocale(locale)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        createConfigurationContext(config)

    resources.updateConfiguration(config, resources.displayMetrics)
}

fun Context.isSupportAuthentication(): Boolean {
    val manager = BiometricManager.from(this)
    val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
    return manager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
}