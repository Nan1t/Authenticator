package ua.nanit.otpmanager.domain

data class Account(
    val name: String,
    val token: String,
    val interval: Int
)

fun Account.code(): String {
    return "000 000"
}