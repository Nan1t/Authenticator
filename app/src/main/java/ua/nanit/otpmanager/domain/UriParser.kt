package ua.nanit.otpmanager.domain

import java.net.URI

object UriParser {

    fun parse(uri: String): UriStruct {
        val parsed = URI.create(uri)
        val args = HashMap<String, String>()
        val pairs = parsed.query.split("&")

        for (pair in pairs) {
            val arr = pair.split("=")
            if (arr.size < 2) continue

            args[arr[0]] = arr[1]
        }

        return UriStruct(parsed, args)
    }

}

data class UriStruct(
    val uri: URI,
    val args: Map<String, String>
)