package ua.nanit.otpmanager.inject

import dagger.Component
import ua.nanit.otpmanager.presentation.accounts.AccountsFragment
import ua.nanit.otpmanager.presentation.create.AddAccountFragment
import ua.nanit.otpmanager.presentation.create.AddHotpFragment
import ua.nanit.otpmanager.presentation.create.AddTotpFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    fun inject(fragment: AddAccountFragment)

    fun inject(fragment: AddTotpFragment)

    fun inject(fragment: AddHotpFragment)

}