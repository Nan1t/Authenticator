package ua.nanit.otpmanager.presentation.accounts

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.Navigator

class AccountsMainMenu(
    private val navigator: Navigator
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuImport -> {
                navigator.navToImport()
                true
            }
            R.id.menuExport -> {
                navigator.navToExport()
                true
            }
            R.id.menuSettings -> {
                navigator.navToSettings()
                true
            }
            R.id.menuAbout -> {
                navigator.navToAbout()
                true
            }
            else -> false
        }
    }
}