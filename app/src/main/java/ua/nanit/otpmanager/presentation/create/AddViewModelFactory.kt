package ua.nanit.otpmanager.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ua.nanit.otpmanager.domain.account.AccountInteractor
import kotlin.coroutines.CoroutineContext

class AddViewModelFactory(
    private val dispatcher: CoroutineContext,
    private val interactor: AccountInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddViewModel(dispatcher, interactor) as T
    }
}