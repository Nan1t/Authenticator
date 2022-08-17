package ua.nanit.otpmanager.presentation.addnew

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.domain.ImageReader
import ua.nanit.otpmanager.domain.account.*
import javax.inject.Inject

class AddViewModel (
    private val dispatcher: CoroutineContext,
    private val interactor: AccountInteractor
) : ViewModel() {

    val success = MutableLiveData<Unit>()
    val error = MutableLiveData<String>()

    fun decodeQrCode(yuvData: ByteArray, width: Int, height: Int) {
        val uri = ImageReader.read(yuvData, width, height) ?: return

        viewModelScope.launch(dispatcher) {
            try {
                interactor.createByUri(uri)
                success.postValue(Unit)
            } catch (e: InvalidUriSchemeException) {
                error.postValue("Invalid URI format")
            } catch (e: ShortNameException) {
                error.postValue("Short name")
            } catch (e: ShortSecretException) {
                error.postValue("Short secret")
            } catch (e: InvalidIntervalException) {
                error.postValue("Invalid interval")
            } catch (th: Throwable) {
                error.postValue(th.message)
            }
        }
    }

    fun createTotp(name: String, secret: String, interval: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                interactor.createTotpAccount(name, secret, Constants.DEFAULT_ALGORITHM, Constants.DEFAULT_DIGITS, interval)
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
        viewModelScope.launch(dispatcher) {
            try {
                interactor.createHotpAccount(name, secret, Constants.DEFAULT_ALGORITHM, Constants.DEFAULT_DIGITS, counter)
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

class AddViewModelFactory @Inject constructor(
    private val interactor: AccountInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddViewModel(Dispatchers.Default, interactor) as T
    }
}