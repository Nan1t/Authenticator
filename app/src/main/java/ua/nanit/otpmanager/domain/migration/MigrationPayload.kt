package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

/**
 * Protobuf entities for Google Authenticator migration format
 * This format is proprietary so there is no link to specification
 */

@Serializable
data class MigrationPayload constructor(
    @ProtoNumber(1)
    val otpParameters: List<OtpParams> = emptyList(),
    @ProtoNumber(2)
    val version: Int = 0,
    @ProtoNumber(3)
    val batchSize: Int = 0,
    @ProtoNumber(4)
    val batchIndex: Int = 0,
    @ProtoNumber(5)
    val batchId: Int = 0,
)

@Serializable
data class OtpParams(
    @ProtoNumber(1)
    val secret: ByteArray = ByteArray(0),
    @ProtoNumber(2)
    val name: String = "",
    @ProtoNumber(3)
    val issuer: String?,
    @ProtoNumber(4)
    val algorithm: MigrationAlgorithm = MigrationAlgorithm.SHA_1,
    @ProtoNumber(5)
    val digits: DigitCount = DigitCount.SIX,
    @ProtoNumber(6)
    val type: OtpType = OtpType.TOTP,
    @ProtoNumber(7)
    // If TOTP, this is period
    val counter: Long?,
)

@Serializable
enum class OtpType {
    @ProtoNumber(0)
    OTP_TYPE_UNSPECIFIED,
    @ProtoNumber(1)
    HOTP,
    @ProtoNumber(2)
    TOTP
}

@Serializable
enum class DigitCount {
    @ProtoNumber(0)
    DIGIT_COUNT_UNSPECIFIED,
    @ProtoNumber(1)
    SIX,
    @ProtoNumber(2)
    EIGHT
}

@Serializable
enum class MigrationAlgorithm {
    @ProtoNumber(0)
    UNSPECIFIED,
    @ProtoNumber(1)
    SHA_1,
    @ProtoNumber(2)
    SHA_256,
    @ProtoNumber(3)
    SHA_512,
    @ProtoNumber(4)
    MD_5,
}