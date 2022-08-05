package ua.nanit.otpmanager.presentation.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ua.nanit.otpmanager.databinding.FragAddTotpBinding

class AddTotpFragment : Fragment() {

    private lateinit var binding: FragAddTotpBinding
    private val viewModel: AddViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAddTotpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.success.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
            Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show()
        }

        binding.extrasLayout.bindToSwitch(binding.showExtras)
        binding.submitBtn.setOnClickListener {
            val name = binding.accountName.text?.toString() ?: ""
            val secret = binding.accountSecret.text?.toString() ?: ""
            val interval = (binding.totpInterval.text?.toString() ?: "").toLong()

            viewModel.createTotp(name, secret, interval)
        }
    }

}