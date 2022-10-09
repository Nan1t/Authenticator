package ua.nanit.otpmanager.presentation.addnew

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.domain.account.*
import ua.nanit.otpmanager.presentation.Event
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val manager: AccountManager
) : ViewModel() {

    private val success = Event<Account>()
    private val error = Event<CreationError>()

    fun observeSuccess(owner: LifecycleOwner, observer: Observer<Account>) {
        success.observe(owner, observer)
    }

    fun observeError(owner: LifecycleOwner, observer: Observer<CreationError>) {
        error.observe(owner, observer)
    }

    fun createByUri(uri: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val created = manager.createByUri(uri)
                success.postValue(created)
            } catch (ex: AccountCreationException) {
                error.postValue(ex.kind)
            } catch (th: Throwable) {
                error.postValue(CreationError.UNDEFINED)
                th.printStackTrace()
            }
        }
    }

    fun createTotp(name: String, secret: String, interval: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                val account = manager.createTotpAccount(
                    name,
                    null,
                    secret,
                    Constants.DEFAULT_ALGORITHM,
                    Constants.DEFAULT_DIGITS,
                    interval
                )
                success.postValue(account)
            } catch (ex: AccountCreationException) {
                error.postValue(ex.kind)
            } catch (th: Throwable) {
                th.printStackTrace()
                error.postValue(CreationError.UNDEFINED)
            }
        }
    }

    fun createHotp(name: String, secret: String, counter: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                val account = manager.createHotpAccount(
                    name,
                    null,
                    secret,
                    Constants.DEFAULT_ALGORITHM,
                    Constants.DEFAULT_DIGITS,
                    counter
                )
                success.postValue(account)
            } catch (ex: AccountCreationException) {
                error.postValue(ex.kind)
            } catch (th: Throwable) {
                th.printStackTrace()
                error.postValue(CreationError.UNDEFINED)
            }
        }
    }
}