package ua.nanit.otpmanager.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.fragmentContainer)
        val toolbarConf = AppBarConfiguration(setOf(R.id.navAccounts))
        binding.toolbar.setupWithNavController(navController, toolbarConf)
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

    override fun navToAbout() {
        TODO("Not yet implemented")
    }

    override fun navUp() {
        navController.navigateUp()
    }

    override fun navUpToMain() {
        navController.popBackStack(R.id.navAccounts, false)
    }
}