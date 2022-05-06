package org.metalscraps.discord.bot.tts.providers.aws.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object AwsSigning {
    const val AWS4_SIGNING_ALGORITHM = "AWS4-HMAC-SHA256"

    private const val HmacSHA256 = "HmacSHA256"

    private val HMAC_SHA256_MAC: ThreadLocal<Mac> = ThreadLocal.withInitial {
        Mac.getInstance(HmacSHA256)
    }

    fun sign(data: ByteArray, key: ByteArray): ByteArray {
        val mac = HMAC_SHA256_MAC.get()
        mac.init(SecretKeySpec(key, HmacSHA256))
        return mac.doFinal(data)
    }

    fun sign(stringData: String, key: ByteArray): ByteArray {
        return sign(stringData.toByteArray(), key)
    }

    fun getSigningKey(
        date: String,
        secretAccessKey: String,
        regionName: String,
        serviceName: String
    ): ByteArray {
        val kSecret = ("AWS4$secretAccessKey").toByteArray()
        val kDate = sign(date, kSecret)
        val kRegion = sign(regionName, kDate)
        val kService = sign(serviceName, kRegion)
        return sign("aws4_request", kService)
    }

    fun buildAuthorizationHeader(
        signature: ByteArray,
        accessKeyId: String,
        scope: String,
        signedHeadersString: String
    ): String {
        return "$AWS4_SIGNING_ALGORITHM " +
                "Credential=$accessKeyId/$scope, " +
                "SignedHeaders=$signedHeadersString, " +
                "Signature=${AwsBinary.toHex(signature)}"
    }
}