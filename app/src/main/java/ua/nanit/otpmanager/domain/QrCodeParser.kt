package ua.nanit.otpmanager.domain

import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.google.zxing.qrcode.QRCodeWriter
import java.net.URI

object QrCodeParser {

    const val IMAGE_SIZE = 512

    private val reader = QRCodeReader()
    private val writer = QRCodeWriter()

    fun readImage(
        yuvData: ByteArray,
        width: Int, height: Int,
        frameX: Int, frameY: Int,
        frameSize: Int,
    ): String? {
        return readImage(yuvData, width, height, frameX, frameY, frameSize, frameSize)
    }

    fun readImage(
        yuvData: ByteArray,
        width: Int, height: Int,
        frameX: Int, frameY: Int,
        frameWidth: Int, frameHeight: Int
    ): String? {
        return try {
            val source = PlanarYUVLuminanceSource(
                yuvData,
                width, height,
                frameX, frameY,
                frameWidth, frameHeight,
                false
            )
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            reader.decode(bitmap).text
        } catch (th: Throwable) {
            null
        }
    }

    fun createImage(uri: URI): BitMatrix {
        return writer.encode(uri.toString(), BarcodeFormat.QR_CODE, IMAGE_SIZE, IMAGE_SIZE)
    }

}