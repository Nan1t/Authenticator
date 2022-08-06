package ua.nanit.otpmanager.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import ua.nanit.otpmanager.domain.account.AccountInteractor
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AddViewModelFactory @Inject constructor(
    private val interactor: AccountInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddViewModel(Dispatchers.IO, interactor) as T
    }
}