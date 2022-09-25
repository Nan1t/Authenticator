package ua.nanit.otpmanager.domain.time

import ua.nanit.otpmanager.domain.account.TotpAccount
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object TotpTimer {

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val listeners = ConcurrentHashMap<String, TotpTask>()

    fun start() {
        scheduler.scheduleAtFixedRate(::tick, 0, 1, TimeUnit.SECONDS)
    }

    fun subscribe(acc: TotpAccount) {
        val task = TotpTask(acc)
        listeners[acc.label] = task
    }

    fun unsubscribe(acc: TotpAccount) {
        listeners.remove(acc.label)
    }

    private fun tick() {
        try {
            listeners.values.forEach(TotpTask::tick)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }
}

interface TotpListener {
    fun onTick(progress: Int)
    fun onUpdate(password: String)
}

class TotpTask(val account: TotpAccount) {

    private var last = account.secondsRemain()

    fun tick() {
        val remain = account.secondsRemain()

        if (remain == last) return

        if (remain > last) {
            account.update()
            account.listener?.onUpdate(account.password)
        }

        last = remain
        account.listener?.onTick(remain - 1)
    }

}