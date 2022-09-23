package ua.nanit.otpmanager.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.AccountManager
import ua.nanit.otpmanager.domain.account.AccountRepository
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AccountRepository,
    private val manager: AccountManager
) : ViewModel() {

    val accounts: StateFlow<List<Account>> = repository.accounts

    fun edit(acc: Account) {
        viewModelScope.launch(dispatcher) {
            repository.edit(acc)
        }
    }

}