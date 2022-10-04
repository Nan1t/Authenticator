package ua.nanit.otpmanager.domain.time

import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object TotpTimer {

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val listeners = LinkedList<TotpListener>()

    fun start() {
        scheduler.scheduleAtFixedRate(::tick, 0, 1, TimeUnit.SECONDS)
    }

    fun subscribe(listener: TotpListener) {
        listeners.add(listener)
    }

    private fun tick() {
        try {
            listeners.forEach(TotpListener::onTick)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }
}

interface TotpListener {
    fun onTick()
}
