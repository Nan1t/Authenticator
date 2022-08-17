package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.database.AccountDao
import ua.nanit.otpmanager.database.AccountEntity
import ua.nanit.otpmanager.database.AccountType
import javax.inject.Inject

class AccountFactory @Inject constructor(
    private val dao: AccountDao,
    private val mapper: AccountMapper
) {

    suspend fun createTotp(
        name: String,
        secret: ByteArray,
        algorithm: String,
        digits: Int,
        interval: Long
    ): Account {
        return create(AccountType.TOTP, name, secret, algorithm, digits, interval)
    }

    suspend fun createHotp(
        name: String,
        secret: ByteArray,
        algorithm: String,
        digits: Int,
        counter: Long
    ): Account {
        return create(AccountType.HOTP, name, secret, algorithm, digits, counter)
    }

    private suspend fun create(
        type: AccountType,
        name: String,
        secret: ByteArray,
        algorithm: String,
        digits: Int,
        interval: Long
    ): Account {
        val entity = AccountEntity(0, type, name, secret, algorithm, digits, interval)
        entity.id = dao.create(entity)
        return mapper.fromEntity(entity)
    }

}