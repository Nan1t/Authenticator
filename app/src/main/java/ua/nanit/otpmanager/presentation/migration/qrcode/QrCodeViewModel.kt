package ua.nanit.otpmanager.presentation.migration.qrcode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.migration.UriMigration
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val migration: UriMigration
) : ViewModel() {

    private var page: Int = 0

    val payload = MutableLiveData<UriMigration.Payload>()

    init {
        this.page = 0
        updatePage()
    }

    fun nextPage() {
        this.page++
        updatePage()
    }

    fun prevPage() {
        this.page--
        updatePage()
    }

    private fun updatePage() {
        page = max(0, page)

        viewModelScope.launch(dispatcher) {
            val data = migration.export(page)

            if (data != null) {
                payload.postValue(data!!)
            }
        }
    }
}