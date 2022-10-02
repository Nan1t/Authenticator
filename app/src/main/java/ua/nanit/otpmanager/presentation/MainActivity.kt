package ua.nanit.otpmanager.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.ActivityMainBinding
import ua.nanit.otpmanager.presentation.ext.appSettings
import ua.nanit.otpmanager.presentation.ext.isSupportAuthentication
import ua.nanit.otpmanager.presentation.ext.updateLocale
import ua.nanit.otpmanager.presentation.ext.updateNightMode

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    companion object {
        private var authenticated = false
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        updateLocale()
        updateNightMode()

        if (!requestAuthentication()) {
            val binding = ActivityMainBinding.inflate(layoutInflater, null, false)
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)

            navController = findNavController(R.id.fragmentContainer)
            val toolbarConf = AppBarConfiguration(setOf(R.id.navAccounts))
            binding.toolbar.setupWithNavController(navController, toolbarConf)
        }
    }

    private fun requestAuthentication(): Boolean {
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL

        if (!appSettings().isProtectAccounts() || authenticated || !isSupportAuthentication())
            return false

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric))
            .setAllowedAuthenticators(authenticators)
            .build()

        val prompt = BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                authenticated = true
                recreate()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                    finish()
                }
            }
        })

        prompt.authenticate(info)
        return true
    }

    override fun navToManualAdd() {
        navController.navigate(R.id.actionNavAddManual)
    }

    override fun navToScanCode() {
        navController.navigate(R.id.actionNavScanCode)
    }

    override fun navToExport() {
        navController.navigate(R.id.actionNavExport)
    }

    override fun navToExportFile() {
        navController.navigate(R.id.actionNavExportFile)
    }

    override fun navToExportQr() {
        navController.navigate(R.id.actionNavExportQr)
    }

    override fun navToImport() {
        navController.navigate(R.id.actionNavImport)
    }

    override fun navToImportFile() {
        navController.navigate(R.id.actionNavImportFile)
    }

    override fun navToImportQr() {
        navController.navigate(R.id.actionNavImportQr)
    }

    override fun navToSettings() {
        navController.navigate(R.id.actionNavSettings)
    }

    override fun navToAbout() {
        navController.navigate(R.id.actionNavAbout)
    }

    override fun navUp() {
        navController.navigateUp()
    }

    override fun navUpToMain() {
        navController.popBackStack(R.id.navAccounts, false)
    }
}