package ua.nanit.otpmanager.domain.account

import kotlinx.serialization.Serializable

@Serializable
abstract class Account {

    abstract val label: String
    abstract val name: String
    abstract val issuer: String?
    abstract val secret: ByteArray
    abstract val algorithm: String
    abstract val digits: Int
    abstract var currentPassword: String

    abstract fun update()
}