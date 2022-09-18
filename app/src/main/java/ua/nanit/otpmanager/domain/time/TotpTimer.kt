package ua.nanit.otpmanager.domain.time

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object TotpTimer {

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val listeners = HashMap<Int, () -> Unit>()
    private var task: ScheduledFuture<*>? = null

    fun start() {
        task = scheduler.scheduleAtFixedRate(::tick, 0, 1, TimeUnit.SECONDS)
    }

    fun subscribe(id: Int, callback: () -> Unit) {
        listeners[id] = callback
    }

    fun unsubscribe(id: Int) {
        listeners.remove(id)
    }

    private fun tick() {
        try {
            listeners.values.forEach { cb -> cb() }
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }

}