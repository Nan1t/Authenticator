package ua.nanit.otpmanager.domain

import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader

class QrImageReader {

    private val reader = QRCodeReader()

    fun read(
        yuvData: ByteArray,
        width: Int,
        height: Int,
        frameX: Int,
        frameY: Int,
        frameSize: Int,
    ): String? {
        return try {
            val source = PlanarYUVLuminanceSource(
                yuvData,
                width, height,
                frameX, frameY,
                frameSize, frameSize,
                false
            )
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            reader.decode(bitmap).text
        } catch (th: Throwable) {
            null
        }
    }

}