package ua.nanit.otpmanager.domain.account

class AccountCreationException(val kind: CreationError) : RuntimeException()

enum class CreationError {
    UNDEFINED,
    ALREADY_EXISTS,
    INVALID_URI,
    UNDEFINED_TYPE,
    SHORT_LABEL,
    SHORT_SECRET,
    INVALID_INTERVAL,
    INVALID_COUNTER;

    fun asException() = AccountCreationException(this)
}