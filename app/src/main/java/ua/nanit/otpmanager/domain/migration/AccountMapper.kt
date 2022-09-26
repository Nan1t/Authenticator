package ua.nanit.otpmanager.domain.migration

import ua.nanit.otpmanager.domain.Constants
import ua.nanit.otpmanager.domain.account.Account
import ua.nanit.otpmanager.domain.account.HotpAccount
import ua.nanit.otpmanager.domain.account.LabelParser
import ua.nanit.otpmanager.domain.account.TotpAccount
import ua.nanit.otpmanager.domain.otp.DigestAlgorithm

class AccountMapper {

    fun mapToOtpParams(account: Account): OtpParams {
        return OtpParams(
            account.secret,
            account.name,
            account.issuer,
            account.algorithm.toMigration(),
            account.digits.toDigitsCount(),
            account.otpType(),
            if (account is HotpAccount) account.counter else null
        )
    }

    fun mapToAccount(params: OtpParams): Account {
        val parsed = LabelParser.parse(params.name)
        val label = LabelParser.build(parsed.name, params.issuer)

        return when (params.type) {
            OtpType.OTP_TYPE_UNSPECIFIED,
            OtpType.TOTP -> {
                TotpAccount(
                    label,
                    parsed.name,
                    params.issuer,
                    params.secret,
                    params.algorithm.toDigest(),
                    params.digits.toNumber(),
                    // Seems like Google Authenticator migration has no interval field
                    Constants.DEFAULT_TOTP_INTERVAL
                )
            }
            OtpType.HOTP -> {
                HotpAccount(
                    label,
                    parsed.name,
                    params.issuer,
                    params.secret,
                    params.algorithm.toDigest(),
                    params.digits.toNumber(),
                    params.counter ?: 0
                )
            }
        }
    }

    private fun Account.otpType(): OtpType {
        return when(this) {
            is HotpAccount -> OtpType.HOTP
            is TotpAccount -> OtpType.TOTP
        }
    }

    private fun Int.toDigitsCount(): DigitCount {
        return when (this) {
            6 -> DigitCount.SIX
            8 -> DigitCount.EIGHT
            else -> DigitCount.DIGIT_COUNT_UNSPECIFIED
        }
    }

    private fun DigitCount.toNumber(): Int {
        return when (this) {
            DigitCount.DIGIT_COUNT_UNSPECIFIED, DigitCount.SIX -> 6
            DigitCount.EIGHT -> 8
        }
    }

    private fun DigestAlgorithm.toMigration(): MigrationAlgorithm {
        return when (this) {
            DigestAlgorithm.SHA_1 -> MigrationAlgorithm.SHA_1
            DigestAlgorithm.SHA_256 -> MigrationAlgorithm.SHA_256
            DigestAlgorithm.SHA_512 -> MigrationAlgorithm.SHA_512
        }
    }

    private fun MigrationAlgorithm.toDigest(): DigestAlgorithm {
        return when (this) {
            MigrationAlgorithm.UNSPECIFIED,
            MigrationAlgorithm.MD_5 -> throw IllegalArgumentException("Undefined algorithm")
            MigrationAlgorithm.SHA_1 -> DigestAlgorithm.SHA_1
            MigrationAlgorithm.SHA_256 -> DigestAlgorithm.SHA_256
            MigrationAlgorithm.SHA_512 -> DigestAlgorithm.SHA_512
        }
    }

}