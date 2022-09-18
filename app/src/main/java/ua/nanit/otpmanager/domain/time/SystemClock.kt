package ua.nanit.otpmanager.domain.time

object SystemClock : Clock {

    override fun epochSeconds(): Long {
        return System.currentTimeMillis() / 1000
    }

}