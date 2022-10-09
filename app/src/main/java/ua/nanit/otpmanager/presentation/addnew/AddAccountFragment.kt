package ua.nanit.otpmanager.presentation.addnew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ua.nanit.otpmanager.R
import ua.nanit.otpmanager.databinding.FragAddBinding
import ua.nanit.otpmanager.presentation.ext.display
import ua.nanit.otpmanager.presentation.ext.navigator

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
        val ctx = requireContext()
        val adapter = PagerAdapter(this)
        val strategy = ConfStrategy(ctx)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager, strategy).attach()

        viewModel.observeSuccess(viewLifecycleOwner) {
            val msg = getString(R.string.accounts_add_success, it.name)
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
            navigator().navUp()
        }

        viewModel.observeError(viewLifecycleOwner) {
            Snackbar.make(view, it.display(ctx), Snackbar.LENGTH_SHORT).show()
        }
    }

}