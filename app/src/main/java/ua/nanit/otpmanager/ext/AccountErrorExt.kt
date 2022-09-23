package ua.nanit.otpmanager.ext

import android.content.Context
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.domain.account.CreationError

val map = hashMapOf(
    CreationError.UNDEFINED to R.string.error_undefined,
    CreationError.ALREADY_EXISTS to R.string.error_exists,
    CreationError.INVALID_URI to R.string.error_invalidUri,
    CreationError.UNDEFINED_TYPE to R.string.error_undefinedType,
    CreationError.SHORT_LABEL to R.string.error_shortLabel,
    CreationError.SHORT_SECRET to R.string.error_shortSecret,
    CreationError.INVALID_INTERVAL to R.string.error_invalidInterval,
    CreationError.INVALID_COUNTER to R.string.error_invalidCounter,
)

fun CreationError.display(ctx: Context): String {
    return ctx.getString(map[this] ?: R.string.error_undefined)
}