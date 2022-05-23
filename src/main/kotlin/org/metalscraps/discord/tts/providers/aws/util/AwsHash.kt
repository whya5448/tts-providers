package org.metalscraps.discord.tts.providers.aws.util

import java.security.MessageDigest

object AwsHash {
    private var SHA256_MESSAGE_DIGEST: ThreadLocal<MessageDigest> = ThreadLocal.withInitial {
        MessageDigest.getInstance("SHA-256")
    }

    fun hash(text: String): ByteArray {
        return hash(text.toByteArray())
    }

    fun hash(data: ByteArray): ByteArray {
        val md = SHA256_MESSAGE_DIGEST.get()
        md.reset()
        md.update(data)
        return md.digest()
    }
}