package ua.nanit.otpmanager.domain.account

import java.util.regex.Pattern

object LabelParser {

    private val labelPattern = Pattern.compile(":")

    fun parse(label: String): ParsedLabel {
        val arr = label.split(labelPattern, 2)
        val name: String
        val issuer: String?

        if (arr.size == 2) {
            name = arr[1]
            issuer = arr[0]
        } else {
            name = arr[0]
            issuer = null
        }

        return ParsedLabel(name, issuer)
    }

    fun build(name: String, issuer: String?): String {
        val builder = StringBuilder()
        if (issuer != null) {
            builder.append(issuer).append(":")
        }
        builder.append(name)
        return builder.toString()
    }
}

class ParsedLabel(val name: String, val issuer: String?)