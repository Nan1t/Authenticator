package ua.nanit.otpmanager.presentation.migration.file

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.DialogBackupPinBinding

class PinDialog(
    ctx: Context,
    hintStr: Int,
    private val onConfirm: (String) -> Unit,
    private val onCancel: () -> Unit
) : AlertDialog(ctx) {

    private val errorText = ctx.getString(R.string.account_migration_pin_invalid)
    private val binding = DialogBackupPinBinding.inflate(LayoutInflater.from(ctx), null, false)

    init {
        binding.hintText.setText(hintStr)
        setTitle(R.string.account_migration_pin)
        setView(binding.root)
        setButton(BUTTON_POSITIVE, ctx.getString(R.string.ok)) { _, _ -> }
        setButton(BUTTON_NEGATIVE, ctx.getString(R.string.cancel)) { _, _ -> onCancel() }
        setCanceledOnTouchOutside(false)
    }

    override fun show() {
        setOnShowListener { dialog ->
            binding.pinCodeLayout.error = null

            getButton(BUTTON_POSITIVE).setOnClickListener {
                val pin = binding.pinCode.text.toString()

                if (pin.length >= 4) {
                    onConfirm(pin)
                    dialog.dismiss()
                } else {
                    binding.pinCodeLayout.error = errorText
                }
            }
        }
        super.show()
    }

}