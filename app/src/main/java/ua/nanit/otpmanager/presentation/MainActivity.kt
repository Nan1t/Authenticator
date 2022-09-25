package ua.nanit.otpmanager.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.fragmentContainer)
        val toolbarConf = AppBarConfiguration(setOf(R.id.navAccounts))
        binding.toolbar.setupWithNavController(navController, toolbarConf)

        navController.addOnDestinationChangedListener { _, dest, _ ->
            when (dest.id) {
                R.id.navScanCode -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }
        }
    }
}