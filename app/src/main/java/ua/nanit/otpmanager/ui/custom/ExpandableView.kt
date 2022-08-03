package ua.nanit.otpmanager.ui.custom

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
        if (visible) {
            this.visibility = VISIBLE
        } else {
            this.visibility = GONE
        }
    }
}