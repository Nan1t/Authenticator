package ua.nanit.otpmanager.presentation.addnew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.databinding.FragAddTotpBinding
import ua.nanit.otpmanager.domain.Constants

@AndroidEntryPoint
class AddTotpFragment : Fragment() {

    private lateinit var binding: FragAddTotpBinding
    private val viewModel: AddViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAddTotpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.totpInterval.setText(Constants.DEFAULT_TOTP_INTERVAL.toString(), TextView.BufferType.NORMAL)
        binding.extrasLayout.bindToSwitch(binding.showExtras)
        binding.submitBtn.setOnClickListener {
            val name = binding.accountName.text?.toString() ?: ""
            val secret = binding.accountSecret.text?.toString() ?: ""
            val interval = (binding.totpInterval.text?.toString() ?: "").toLong()

            viewModel.createTotp(name, secret, interval)
        }
    }
}