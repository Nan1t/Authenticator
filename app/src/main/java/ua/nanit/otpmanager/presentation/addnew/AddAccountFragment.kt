package ua.nanit.otpmanager.presentation.addnew

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import ua.nanit.otpmanager.appComponent
import ua.nanit.otpmanager.databinding.FragAddBinding
import javax.inject.Inject

class AddAccountFragment : Fragment() {

    private lateinit var binding: FragAddBinding
    private val viewModel: AddViewModel by viewModels { viewModelFactory }

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