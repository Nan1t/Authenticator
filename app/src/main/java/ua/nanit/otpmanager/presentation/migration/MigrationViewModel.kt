package ua.nanit.otpmanager.presentation.migration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.migration.MigrationManager
import ua.nanit.otpmanager.domain.migration.Payload
import java.net.URI
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class MigrationViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val migrationManager: MigrationManager
) : ViewModel() {

    private var page: Int = 0

    val payload = MutableLiveData<Payload>()

    init {
        this.page = 0
        updatePage()
    }

    fun updatePage() {
        page = max(0, page)

        viewModelScope.launch(dispatcher) {
            val data = migrationManager.getPayload(page)

            if (data != null) {
                payload.postValue(data!!)
            }
        }
    }

    fun nextPage() {
        this.page++
        updatePage()
    }

    fun prevPage() {
        this.page--
        updatePage()
    }
}