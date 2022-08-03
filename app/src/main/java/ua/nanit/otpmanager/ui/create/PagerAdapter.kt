package ua.nanit.otpmanager.ui.create

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ua.nanit.otpmanager.R

class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddTotpFragment()
            1 -> AddHotpFragment()
            else -> throw IllegalArgumentException("Out of bound position number")
        }
    }
}

class ConfStrategy(private val ctx: Context) : TabLayoutMediator.TabConfigurationStrategy {

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        when (position) {
            0 -> tab.text= ctx.getString(R.string.manual_tab_totp)
            1 -> tab.text = ctx.getString(R.string.manual_tab_hotp)
        }
    }
}