package ua.nanit.otpmanager.presentation.migration

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.nanit.otpmanager.databinding.FragExportQrBinding
import ua.nanit.otpmanager.domain.QrCodeParser
import java.net.URI

@AndroidEntryPoint
class ExportQrCodeFragment : Fragment() {

    private val viewModel: MigrationViewModel by viewModels()

    private lateinit var binding: FragExportQrBinding
    private lateinit var bitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragExportQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bitmap = Bitmap.createBitmap(QrCodeParser.IMAGE_SIZE, QrCodeParser.IMAGE_SIZE, Bitmap.Config.RGB_565)

        binding.progressBar.visibility = View.VISIBLE
        binding.codeImage.visibility = View.GONE
        binding.prevBtn.visibility = View.GONE
        binding.nextBtn.visibility = View.GONE

        viewModel.payload.observe(viewLifecycleOwner) {
            displayQrImage(it)
        }
    }

    private fun displayQrImage(uri: URI) {
        lifecycleScope.launch(Dispatchers.IO) {
            val matrix = QrCodeParser.createImage(uri)

            for (x in 0 until matrix.width) {
                for (y in 0 until matrix.height) {
                    val color = if (matrix.get(x, y)) Color.BLACK else Color.WHITE
                    bitmap.setPixel(x, y, color)
                }
            }

            binding.codeImage.post {
                binding.progressBar.visibility = View.GONE
                binding.codeImage.visibility = View.VISIBLE
                binding.codeImage.setImageBitmap(bitmap)
            }
        }
    }

}