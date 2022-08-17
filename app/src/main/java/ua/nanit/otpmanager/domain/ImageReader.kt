package ua.nanit.otpmanager.domain

import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader

object ImageReader {

    private val reader = QRCodeReader()

    fun read(yuvData: ByteArray, width: Int, height: Int): String? {
        return try {
            val source = PlanarYUVLuminanceSource(
                yuvData,
                width, height,
                0, 0,
                width, height,
                false
            )
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            reader.decode(bitmap).text
        } catch (th: Throwable) {
            null
        }
    }

}