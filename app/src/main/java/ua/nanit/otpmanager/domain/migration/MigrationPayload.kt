package ua.nanit.otpmanager.domain.migration

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoIntegerType
import kotlinx.serialization.protobuf.ProtoNumber
import kotlinx.serialization.protobuf.ProtoPacked
import kotlinx.serialization.protobuf.ProtoType

@Serializable
data class MigrationPayload constructor(
    @ProtoNumber(1)
    @ProtoPacked
    val otpParameters: List<OtpParams>,
    @ProtoNumber(2)
    val version: Int,
    @ProtoNumber(3)
    val batchSize: Int,
    @ProtoNumber(4)
    val batchIndex: Int,
    @ProtoNumber(5)
    val batchId: Int,
)

@Serializable
data class OtpParams(
    @ProtoNumber(1)
    val secret: ByteArray,
    @ProtoNumber(2)
    val name: String,
    @ProtoNumber(3)
    val issuer: String?,
    @ProtoNumber(4)
    val algorithm: MigrationAlgorithm,
    @ProtoNumber(5)
    val digits: DigitCount,
    @ProtoNumber(6)
    val type: OtpType,
    @ProtoNumber(7)
    val counter: Long?,
)