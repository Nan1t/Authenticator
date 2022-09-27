package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoPacked
import kotlinx.serialization.protobuf.ProtoType

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
    val counter: Long?,
)