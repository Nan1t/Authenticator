package ua.nanit.otpmanager.domain.migration

import ua.nanit.otpmanager.domain.account.AccountRepository

abstract class AbstractMigration(
    private val repository: AccountRepository
) {

    private val mapper = AccountMapper()

    protected suspend fun importPayload(payload: MigrationPayload): Int {
        val accounts = payload.otpParameters.map(mapper::mapToAccount)
        repository.addAll(accounts)
        return accounts.size
    }

    protected fun getOtpParams(): List<OtpParams> =
        repository.getAll().map(mapper::mapToOtpParams)

}