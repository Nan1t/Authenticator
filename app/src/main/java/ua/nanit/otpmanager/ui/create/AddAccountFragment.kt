package ua.nanit.otpmanager.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ua.nanit.otpmanager.databinding.FragAddBinding

class AddAccountFragment : Fragment() {

    private lateinit var binding: FragAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = PagerAdapter(this)
        val strategy = ConfStrategy(requireContext())
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager, strategy).attach()
    }

}