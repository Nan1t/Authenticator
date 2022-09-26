package ua.nanit.otpmanager.presentation.ext

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.Navigator

fun Fragment.showCloseableSnackbar(msg: String) {
    val snackbar = Snackbar.make(requireView(), msg, Snackbar.LENGTH_INDEFINITE)
    snackbar.setAction(R.string.ok) { snackbar.dismiss() }
    snackbar.show()
}

fun Fragment.showCloseableSnackbar(@StringRes redId: Int) {
    val snackbar = Snackbar.make(requireView(), redId, Snackbar.LENGTH_INDEFINITE)
    snackbar.setAction(R.string.ok) { snackbar.dismiss() }
    snackbar.show()
}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}