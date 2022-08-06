package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.database.AccountDao
import javax.inject.Inject

class AccountEditor @Inject constructor(
    private val dao: AccountDao,
    private val mapper: AccountMapper
) {

    suspend fun editAccount(account: Account) {
        dao.update(mapper.toEntity(account))
    }

    suspend fun removeAccount(account: Account) {
        dao.delete(mapper.toEntity(account))
    }

}