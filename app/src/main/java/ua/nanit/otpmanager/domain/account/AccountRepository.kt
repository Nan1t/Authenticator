package ua.nanit.otpmanager.domain.account

import kotlinx.coroutines.flow.MutableStateFlow
import ua.nanit.otpmanager.domain.storage.AccountStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val storage: AccountStorage
) {

    val accounts = MutableStateFlow(storage.getAll())

    fun get(label: String): Account? {
        return storage.get(label)
    }

    suspend fun add(account: Account) {
        storage.add(account)
        emitUpdate()
    }

    suspend fun edit(account: Account) {
        storage.edit(account)
        emitUpdate()
    }

    suspend fun remove(account: Account) {
        storage.remove(account)
        emitUpdate()
    }

    private suspend fun emitUpdate() {
        accounts.emit(storage.getAll())
    }

}