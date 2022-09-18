package ua.nanit.otpmanager.domain.time

interface Clock {

    fun epochSeconds(): Long

}