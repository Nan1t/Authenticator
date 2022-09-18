package ua.nanit.otpmanager

import ua.nanit.otpmanager.domain.time.Clock

class ConstClock(private val value: Long) : Clock {

    override fun epochSeconds(): Long = value

}