package ua.nanit.otpmanager.presentation.settings

import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.presentation.ext.isSupportAuthentication
import ua.nanit.otpmanager.presentation.ext.showCloseableSnackbar

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val activity = requireActivity()

        addPreferencesFromResource(R.xml.settings)

        val nightMode = findPreference<SwitchPreference>("nightMode")!!
        val securityMode = findPreference<SwitchPreference>("securityMode")!!
        val locale = findPreference<ListPreference>("locale")!!

        locale.entries = resources.getStringArray(R.array.locales)
        locale.entryValues = resources.getStringArray(R.array.localeValues)

        nightMode.setOnPreferenceChangeListener { _, _ ->
            ActivityCompat.recreate(activity)
            true
        }

        securityMode.setOnPreferenceChangeListener { _, newValue ->
            if (!(newValue as Boolean) || activity.isSupportAuthentication()) {
                true
            } else {
                showCloseableSnackbar(R.string.settings_security_unavailable)
                false
            }
        }

        locale.setOnPreferenceChangeListener { _, _ ->
            ActivityCompat.recreate(activity)
            true
        }
    }
}