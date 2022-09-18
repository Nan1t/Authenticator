package ua.nanit.otpmanager.presentation.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.domain.account.AccountManager
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AccountsViewModel @Inject constructor(
    private val dispatcher: CoroutineContext,
    private val interactor: AccountManager
) : ViewModel() {


    fun removeAccount() {
        viewModelScope.launch(dispatcher) {

        }
    }

    fun updateAccount(wrapper: AccountItem) {
        viewModelScope.launch(dispatcher) {

        }
    }
}