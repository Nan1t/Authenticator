package ua.nanit.otpmanager.domain.account

import ua.nanit.otpmanager.database.AccountEntity
import ua.nanit.otpmanager.database.AccountType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountMapper @Inject constructor() {

    fun fromEntity(entity: AccountEntity): Account {
        return when (entity.type) {
            AccountType.TOTP -> TotpAccount(
                entity.id.toInt(), entity.name, entity.secret, entity.counter
            )
            AccountType.HOTP -> HotpAccount(
                entity.id.toInt(), entity.name, entity.secret, entity.counter
            )
        }
    }

    fun toEntity(account: Account): AccountEntity {
        return when(account) {
            is TotpAccount -> AccountEntity(
                account.id.toLong(),
                AccountType.TOTP,
                account.name,
                account.secret,
                account.interval
            )
            is HotpAccount -> AccountEntity(
                account.id.toLong(),
                AccountType.HOTP,
                account.name,
                account.secret,
                account.counter
            )
        }
    }

}