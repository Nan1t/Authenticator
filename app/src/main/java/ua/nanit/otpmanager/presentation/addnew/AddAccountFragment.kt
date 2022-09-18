package ua.nanit.otpmanager.presentation.addnew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.databinding.FragAddBinding

@AndroidEntryPoint
class AddAccountFragment : Fragment() {

    private lateinit var binding: FragAddBinding
    private val viewModel: AddViewModel by activityViewModels()

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

        viewModel.success.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
            Snackbar.make(view, "Success", Snackbar.LENGTH_SHORT).show()
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        }
    }

}