package ua.nanit.otpmanager.presentation.create

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.databinding.FragAddBinding

class AddAccountFragment : Fragment() {

    private lateinit var binding: FragAddBinding
    private val viewModel: AddViewModel by viewModels()

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