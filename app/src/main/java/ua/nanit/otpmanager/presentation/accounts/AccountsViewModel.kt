package ua.nanit.otpmanager.presentation.accounts

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.AccountRepository
import ua.nanit.otpmanager.presentation.Event
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AccountRepository,
) : ViewModel() {

    private val accounts: StateFlow<List<Account>> = repository.accounts
    private val updateResult = Event<Account>()

    suspend fun observeAccounts(callback: (List<Account>) -> Unit) {
        accounts.collect(callback)
    }

    fun observeUpdate(owner: LifecycleOwner, observer: Observer<Account>) {
        updateResult.observe(owner, observer)
    }

    fun edit(acc: Account) {
        viewModelScope.launch(dispatcher) {
            repository.edit(acc)
            updateResult.postValue(acc)
        }
    }

    fun delete(acc: Account) {
        viewModelScope.launch(dispatcher) {
            repository.delete(acc)
        }
    }

}