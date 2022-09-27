package ua.nanit.otpmanager.domain.migration

import ua.nanit.otpmanager.domain.account.AccountRepository

abstract class AbstractMigration(
    private val repository: AccountRepository
) {

    private val mapper = AccountMapper()

    protected fun getOtpParams(): List<OtpParams> =
        repository.getAll().map(mapper::mapToOtpParams)

}