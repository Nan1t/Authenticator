package ua.nanit.otpmanager.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ua.nanit.otpmanager.BuildConfig
import ua.nanit.otpmanager.R

class AboutFragment : PreferenceFragmentCompat() {

    private val privacyUrl = lazy { Uri.parse(getString(R.string.about_privacy_url)) }
    private val sourcesUrl = lazy { Uri.parse(getString(R.string.about_sources_url)) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.about)
        findPreference<Preference>("version")?.summary = BuildConfig.VERSION_NAME
        findPreference<Preference>("privacy")?.setOnPreferenceClickListener {
            openInBrowser(privacyUrl.value)
            false
        }
        findPreference<Preference>("sources")?.setOnPreferenceClickListener {
            openInBrowser(sourcesUrl.value)
            false
        }
    }

    private fun openInBrowser(url: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, url))
    }
}