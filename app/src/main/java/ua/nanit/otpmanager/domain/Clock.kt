package ua.nanit.otpmanager.domain

object Clock {

    fun epochMillis() = System.currentTimeMillis()

    fun epochSeconds() = epochMillis() / 1000

}
