package ua.nanit.otpmanager.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.SwitchCompat

class ExpandableView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(ctx, attrs, defStyle) {

    init {
        setVisible(false)
    }

    fun bindToSwitch(switch: SwitchCompat) {
        switch.setOnCheckedChangeListener { _, checked ->
            setVisible(checked)
        }
    }

    private fun setVisible(visible: Boolean) {
        this.visibility = if (visible) VISIBLE else GONE
    }
}