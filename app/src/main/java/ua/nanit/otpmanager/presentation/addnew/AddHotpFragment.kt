package ua.nanit.otpmanager.presentation.addnew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.databinding.FragAddHotpBinding

@AndroidEntryPoint
class AddHotpFragment : Fragment() {

    private lateinit var binding: FragAddHotpBinding
    private val viewModel: AddViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAddHotpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.extrasLayout.bindToSwitch(binding.showExtras)
        binding.submitBtn.setOnClickListener {
            val name = binding.accountName.text?.toString() ?: ""
            val secret = binding.accountSecret.text?.toString() ?: ""
            val counter = (binding.hotpCounter.text?.toString() ?: "").toLong()

            viewModel.createHotp(name, secret, counter)
        }
    }
}