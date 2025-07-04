package ua.nanit.otpmanager.presentation.migration.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ua.nanit.otpmanager.databinding.FragProgressBinding

abstract class FileMigrationFragment : Fragment() {

    private lateinit var binding: FragProgressBinding
    private lateinit var permLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        permLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            onPermissionResult(it)
        }
    }

    protected abstract fun onPermissionResult(result: Boolean)

    protected fun setProcessing(isProcessing: Boolean) {
        if (isProcessing) {
            binding.wait.visibility = getVisibility(true)
            binding.progressBar.visibility = getVisibility(true)
        } else {
            binding.wait.visibility = getVisibility(false)
            binding.progressBar.visibility = getVisibility(false)
        }
    }

    private fun getVisibility(isVisible: Boolean) =
        if (isVisible) View.VISIBLE else View.GONE
}