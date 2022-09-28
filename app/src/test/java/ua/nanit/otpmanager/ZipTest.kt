package ua.nanit.otpmanager

import net.lingala.zip4j.io.outputstream.ZipOutputStream
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.EncryptionMethod
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ZipTest {

    @Test
    fun testZIp() {
        val bytes = "Hello, world!".toByteArray()
        val zipParams = ZipParameters().apply {
            isEncryptFiles = true
            encryptionMethod = EncryptionMethod.AES
            fileNameInZip = "hello.txt"
        }
        val file = File("D:\\test.zip")

        if (!file.exists())
            file.createNewFile()

        //val stream = ByteArrayOutputStream()
        val stream = FileOutputStream(file)
        val result = ZipOutputStream(stream, "1111".toCharArray()).use { zipOs ->
            zipOs.putNextEntry(zipParams)
            zipOs.write(bytes, 0, bytes.size)
            zipOs.closeEntry()

            //stream.toByteArray()
        }

        //file.writeBytes(result)
    }

}