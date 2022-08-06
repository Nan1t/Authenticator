package ua.nanit.otpmanager

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import ua.nanit.otpmanager.inject.AppComponent
import ua.nanit.otpmanager.inject.DaggerAppComponent
import ua.nanit.otpmanager.inject.DataModule

class App : Application() {

   lateinit var component: AppComponent

   override fun onCreate() {
      super.onCreate()

      component = DaggerAppComponent.builder()
         .dataModule(DataModule(this))
         .build()
   }

}

fun Context.appComponent(): AppComponent {
   return when(this) {
      is App -> component
      else -> (applicationContext as App).component
   }
}

fun Fragment.appComponent(): AppComponent {
   return requireContext().appComponent()
}