package ua.nanit.otpmanager.domain.migration

import ua.nanit.otpmanager.domain.account.AccountRepository

abstract class AbstractMigration(
    private val repository: AccountRepository
) {

    private val mapper = AccountMapper()

    protected suspend fun importPayload(payload: MigrationPayload) {
        val accounts = payload.otpParameters.map(mapper::mapToAccount)

        for (account in accounts) {
            repository.add(account)
        }
    }

    protected fun getOtpParams(): List<OtpParams> =
        repository.getAll().map(mapper::mapToOtpParams)

}