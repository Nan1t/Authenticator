package ua.nanit.otpmanager.presentation.ext

import androidx.lifecycle.MutableLiveData

suspend fun catchError(errorLd: MutableLiveData<String>, runnable: suspend () -> Unit) {
    try {
        runnable()
    } catch (ex: Exception) {
        errorLd.postValue(ex.message ?: "")
    }
}