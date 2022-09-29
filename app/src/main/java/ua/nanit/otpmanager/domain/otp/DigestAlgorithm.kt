package ua.nanit.otpmanager.domain.otp

import kotlinx.serialization.Serializable

@Serializable
enum class DigestAlgorithm(val value: String) {
    MD_5("MD5"),
    SHA_1("SHA1"),
    SHA_256("SHA256"),
    SHA_512("SHA512");

    companion object {
        fun fromValue(value: String): DigestAlgorithm? {
            return when (value.lowercase()) {
                "md5", "md-5", "md_5" -> MD_5
                "sha1", "sha-1", "sha_1" -> SHA_1
                "sha256", "sha-256", "sha_256" -> SHA_256
                "sha512", "sha-512", "sha_512" -> SHA_512
                else -> null
            }
        }
    }
}