package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.Serializable

@Serializable
sealed class Account {

    abstract val label: String
    abstract var name: String
    abstract val issuer: String?
    abstract val secret: ByteArray
    abstract val algorithm: String
    abstract val digits: Int

    abstract var password: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Account

        return label == other.label
                && name == other.name
                && issuer == other.issuer
                && secret.contentEquals(other.secret)
                && algorithm == other.algorithm
                && digits == other.digits
    }

    override fun hashCode(): Int {
        var result = label.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (issuer?.hashCode() ?: 0)
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + algorithm.hashCode()
        result = 31 * result + digits
        return result
    }


}