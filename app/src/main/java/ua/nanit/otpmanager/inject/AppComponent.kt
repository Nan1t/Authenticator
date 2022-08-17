package ua.nanit.otpmanager.inject

import dagger.Component
import ua.nanit.otpmanager.presentation.accounts.AccountsFragment
import ua.nanit.otpmanager.presentation.addnew.AddAccountFragment
import ua.nanit.otpmanager.presentation.addnew.AddHotpFragment
import ua.nanit.otpmanager.presentation.addnew.AddTotpFragment
import ua.nanit.otpmanager.presentation.addnew.ScanCodeActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {

    fun inject(fragment: AccountsFragment)

    fun inject(fragment: AddAccountFragment)

    fun inject(fragment: AddTotpFragment)

    fun inject(fragment: AddHotpFragment)

    fun inject(activity: ScanCodeActivity)

}