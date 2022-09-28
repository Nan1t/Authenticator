package ua.nanit.otpmanager.presentation.migration.file

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.migration.FileMigration
import ua.nanit.otpmanager.presentation.Event
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class FileMigrationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fileMigration: FileMigration
) : ViewModel() {

    val exportResult = MutableLiveData<String>()
    val importResult = MutableLiveData<Int>()
    val errorResult = Event<String>()

    fun export(pin: String) {
        viewModelScope.launch(dispatcher) {
            catchError(errorResult) {
                exportResult.postValue(fileMigration.export(pin))
            }
        }
    }

    fun import(input: InputStream, pin: String) {
        viewModelScope.launch(dispatcher) {
            catchError(errorResult) {
                importResult.postValue(fileMigration.import(input, pin))
            }
        }
    }
}

suspend fun catchError(errorLd: MutableLiveData<String>, runnable: suspend () -> Unit) {
    try {
        runnable()
    } catch (ex: Exception) {
        errorLd.postValue(ex.message ?: "")
    }
}