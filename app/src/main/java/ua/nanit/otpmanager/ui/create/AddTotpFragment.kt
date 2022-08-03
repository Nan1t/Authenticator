package ua.nanit.otpmanager.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ua.nanit.otpmanager.databinding.FragAddTotpBinding

class AddTotpFragment : Fragment() {

    private lateinit var binding: FragAddTotpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAddTotpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.extrasLayout.bindToSwitch(binding.showExtras)
    }

}