package ua.nanit.otpmanager.presentation.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.account.*

class AddViewModel (
    private val dispatcher: CoroutineContext,
    private val interactor: AccountInteractor
) : ViewModel() {

    val success = MutableLiveData<Unit>()
    val error = MutableLiveData<String>()

    fun createTotp(name: String, secret: String, interval: Long) {
        println("Create TOTP $name, $secret, $interval")

        viewModelScope.launch(dispatcher) {
            try {
                interactor.createTotpAccount(name, secret, interval)
                success.postValue(Unit)
            } catch (e: ShortNameException) {
                error.postValue("Short name")
            } catch (e: ShortSecretException) {
                error.postValue("Short secret")
            } catch (e: InvalidIntervalException) {
                error.postValue("Invalid interval")
            }
        }
    }

    fun createHotp(name: String, secret: String, counter: Long) {
        println("Create HOTP $name, $secret, $counter")

        viewModelScope.launch(dispatcher) {
            try {
                interactor.createHotpAccount(name, secret, counter)
                success.postValue(Unit)
            } catch (e: ShortNameException) {
                error.postValue("Short name")
            } catch (e: ShortSecretException) {
                error.postValue("Short secret")
            } catch (e: InvalidCounterException) {
                error.postValue("Invalid counter")
            }
        }
    }
}