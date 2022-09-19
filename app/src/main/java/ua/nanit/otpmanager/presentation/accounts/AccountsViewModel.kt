package ua.nanit.otpmanager.presentation.accounts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.AccountManager
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val manager: AccountManager
) : ViewModel() {

    val accounts = MutableLiveData<List<Account>>()

    init {
        viewModelScope.launch(dispatcher) {
            accounts.postValue(manager.getAll())
        }
    }

    fun update(acc: Account) {
        viewModelScope.launch(dispatcher) {
            manager.save(acc)
        }
    }

}