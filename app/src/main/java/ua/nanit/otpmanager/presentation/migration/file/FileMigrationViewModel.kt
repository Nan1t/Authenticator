package ua.nanit.otpmanager.presentation.migration.file

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.migration.FileMigration
import ua.nanit.otpmanager.presentation.Event
import ua.nanit.otpmanager.presentation.ext.catchError
import javax.inject.Inject

@HiltViewModel
class FileMigrationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val fileMigration: FileMigration
) : ViewModel() {

    private val errorResult = Event<String>()
    private val exportResult = MutableLiveData<String>()
    private val importResult = MutableLiveData<Int>()
    private val fileResult = MutableLiveData<Uri>()

    private var pinCode: String? = null

    fun observeErrorResult(owner: LifecycleOwner, observer: Observer<String>) {
        errorResult.observe(owner, observer)
    }

    fun observeExportResult(owner: LifecycleOwner, observer: Observer<String>) {
        exportResult.observe(owner, observer)
    }

    fun observeImportResult(owner: LifecycleOwner, observer: Observer<Int>) {
        importResult.observe(owner, observer)
    }

    fun observeFileResult(owner: LifecycleOwner, observer: Observer<Uri>) {
        fileResult.observe(owner, observer)
    }

    fun savePin(pin: String) {
        pinCode = pin
    }

    fun export(fileUri: Uri) {
        val pin = pinCode ?: return

        viewModelScope.launch(dispatcher) {
            catchError(errorResult) {
                exportResult.postValue(fileMigration.export(pin, fileUri))
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

    fun selectFile(fileUri: Uri) {
        fileResult.value = fileUri
    }
}