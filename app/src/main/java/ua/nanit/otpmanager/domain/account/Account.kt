package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.Serializable

@Serializable
sealed class Account {
    abstract val id: Int
    abstract val name: String
    abstract val secret: ByteArray
    abstract val algorithm: String
    abstract val digits: Int
    abstract var currentPassword: String

    abstract fun update()
}