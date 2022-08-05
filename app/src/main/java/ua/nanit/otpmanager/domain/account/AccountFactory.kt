package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.database.AccountDao
import ua.nanit.otpmanager.database.AccountEntity
import ua.nanit.otpmanager.database.AccountType

class AccountFactory(
    private val dao: AccountDao,
    private val mapper: AccountMapper
) {

    suspend fun createTotp(name: String, secret: ByteArray, interval: Long): Account {
        val entity = AccountEntity(0, AccountType.TOTP, name, secret, interval)
        entity.id = dao.create(entity)
        return mapper.fromEntity(entity)
    }

    suspend fun createHotp(name: String, secret: ByteArray, counter: Long): Account {
        val entity = AccountEntity(0, AccountType.HOTP, name, secret, counter)
        entity.id = dao.create(entity)
        return mapper.fromEntity(entity)
    }

}