package ua.nanit.otpmanager.presentation.migration.file

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.migration.FileMigration
import ua.nanit.otpmanager.presentation.Event
import ua.nanit.otpmanager.presentation.ext.catchError
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class FileMigrationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fileMigration: FileMigration
) : ViewModel() {

    val errorResult = Event<String>()
    val exportResult = MutableLiveData<String>()
    val importResult = MutableLiveData<Int>()
    val fileResult = MutableLiveData<InputStream>()

    fun export(pin: String) {
        viewModelScope.launch(dispatcher) {
            catchError(errorResult) {
                exportResult.postValue(fileMigration.export(pin))
            }
        }
    }

    fun import(pin: String) {
        viewModelScope.launch(dispatcher) {
            catchError(errorResult) {
                val stream = fileResult.value
                    ?: throw IllegalArgumentException("File is not selected")

                importResult.postValue(fileMigration.import(stream, pin))
            }
        }
    }

    fun selectFile(stream: InputStream) {
        fileResult.value = stream
    }
}