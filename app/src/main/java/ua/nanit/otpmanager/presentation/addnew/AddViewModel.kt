package ua.nanit.otpmanager.presentation.addnew

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.domain.QrImageReader
import ua.nanit.otpmanager.domain.account.*
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val manager: AccountManager
) : ViewModel() {

    val success = MutableLiveData<Unit>()
    val error = MutableLiveData<String>()

    private var blockedForDecoding = false

    fun decodeQrCode(
        yuvData: ByteArray,
        width: Int,
        height: Int,
        frameX: Int,
        frameY: Int,
        frameSize: Int,
    ) {
        if (blockedForDecoding) return

        val uri = QrImageReader.read(yuvData, width, height, frameX, frameY, frameSize) ?: return

        blockedForDecoding = true

        viewModelScope.launch(dispatcher) {
            try {
                manager.createByUri(uri)
                success.postValue(Unit)
            } catch (e: InvalidUriSchemeException) {
                blockedForDecoding = false
                error.postValue("Invalid URI format")
            } catch (e: ShortLabelException) {
                blockedForDecoding = false
                error.postValue("Short name")
            } catch (e: ShortSecretException) {
                blockedForDecoding = false
                error.postValue("Short secret")
            } catch (e: InvalidIntervalException) {
                blockedForDecoding = false
                error.postValue("Invalid interval")
            } catch (th: Throwable) {
                blockedForDecoding = false
                error.postValue(th.message)
            }
        }
    }

    fun createTotp(name: String, secret: String, interval: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                manager.createTotpAccount(name, null, secret, Constants.DEFAULT_ALGORITHM, Constants.DEFAULT_DIGITS, interval)
                success.postValue(Unit)
            } catch (e: ShortLabelException) {
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
                manager.createHotpAccount(name, null, secret, Constants.DEFAULT_ALGORITHM, Constants.DEFAULT_DIGITS, counter)
                success.postValue(Unit)
            } catch (e: ShortLabelException) {
                error.postValue("Short name")
            } catch (e: ShortSecretException) {
                error.postValue("Short secret")
            } catch (e: InvalidCounterException) {
                error.postValue("Invalid counter")
            }
        }
    }
}