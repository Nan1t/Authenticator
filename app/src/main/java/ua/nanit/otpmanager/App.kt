package ua.nanit.otpmanager

import android.app.Application
import ua.nanit.otpmanager.inject.DaggerAppComponent

class App : Application() {

   val component = DaggerAppComponent.create()

}