package ua.nanit.otpmanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ua.nanit.otpmanager.domain.time.TotpTimer
import ua.nanit.otpmanager.presentation.ext.settingsPreferences
import ua.nanit.otpmanager.presentation.ext.updateNightMode

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TotpTimer.start()
        updateNightMode()
    }

}