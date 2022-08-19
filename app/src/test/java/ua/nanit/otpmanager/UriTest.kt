package ua.nanit.otpmanager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.Test

class UriTest {

    private val flow: Flow<String> = flow {
        repeat(5) {
            emit("Value $it")
        }
    }

    @Test
    fun test() {

    }

}