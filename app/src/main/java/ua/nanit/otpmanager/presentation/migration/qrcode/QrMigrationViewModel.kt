package ua.nanit.otpmanager.presentation.migration.qrcode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.migration.UriMigration
import ua.nanit.otpmanager.presentation.Event
import ua.nanit.otpmanager.presentation.ext.catchError
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class QrMigrationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val migration: UriMigration
) : ViewModel() {

    private var page: Int = 0

    val errorResult = Event<String>()
    val exportPayload = MutableLiveData<UriMigration.Payload>()
    val importResult = Event<UriMigration.ImportResult>()

    init {
        this.page = 0
        updatePage()
    }

    fun import(uri: String) {
        viewModelScope.launch(dispatcher) {
            catchError(errorResult) {
                importResult.postValue(migration.import(uri))
            }
        }
    }

    fun exportNextPage() {
        this.page++
        updatePage()
    }

    fun exportPrevPage() {
        this.page--
        updatePage()
    }

    private fun updatePage() {
        page = max(0, page)

        viewModelScope.launch(dispatcher) {
            val data = migration.export(page)

            if (data != null) {
                exportPayload.postValue(data!!)
            }
        }
    }
}