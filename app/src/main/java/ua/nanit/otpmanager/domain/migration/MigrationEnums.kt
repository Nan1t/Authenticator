package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

/**
 * Protobuf enums for Google Authenticator migration format
 * This format is proprietary so there is no link to specification
 */

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