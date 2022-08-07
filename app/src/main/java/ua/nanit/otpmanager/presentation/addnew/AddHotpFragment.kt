package ua.nanit.otpmanager.presentation.addnew

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ua.nanit.otpmanager.appComponent
import ua.nanit.otpmanager.databinding.FragAddHotpBinding
import javax.inject.Inject

class AddHotpFragment : Fragment() {

    private lateinit var binding: FragAddHotpBinding
    private val viewModel: AddViewModel by viewModels(
        factoryProducer = { viewModelFactory },
        ownerProducer = { requireParentFragment() }
    )

    @Inject
    lateinit var viewModelFactory: AddViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().inject(this)
    }

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