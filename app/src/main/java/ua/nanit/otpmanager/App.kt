package ua.nanit.otpmanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ua.nanit.otpmanager.domain.time.TotpTimer

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TotpTimer.start()
    }

}