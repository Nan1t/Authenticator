package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.database.AccountDao
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val dao: AccountDao,
    private val mapper: AccountMapper
) {

    suspend fun getAll(): List<Account> {
        return dao.getAll().map(mapper::fromEntity)
    }

    suspend fun get(id: Int): Account? {
        val entity = dao.findById(id) ?: return null
        return mapper.fromEntity(entity)
    }
}