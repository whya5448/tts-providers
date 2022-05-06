package org.metalscraps.discord.bot.tts.providers.aws.util

object AwsBinary {
    private const val MASK_4BITS = (1 shl 4) - 1
    private val alphabets: ByteArray

    init {
        val sources = "0123456789abcdef".toCharArray()
        val alphabets = ByteArray(sources.size)
        for (i in alphabets.indices) {
            alphabets[i] = sources[i].code.toByte()
        }

        this.alphabets = alphabets
    }

    private fun toStringDirect(bytes: ByteArray): String {
        val dest = CharArray(bytes.size)
        var i = 0
        for (b in bytes) {
            dest[i++] = Char(b.toUShort())
        }
        return String(dest)
    }

    fun toHex(bytes: ByteArray): String {
        return toStringDirect(encode(bytes))
    }

    private fun encode(src: ByteArray): ByteArray {
        val dest = ByteArray(src.size * 2)
        var p: Byte
        var i = 0
        var j = 0
        while (i < src.size) {
            p = src[i]
            dest[j++] = alphabets[p.toInt() ushr 4 and MASK_4BITS]
            dest[j++] = alphabets[p.toInt() and MASK_4BITS]
            i++
        }
        return dest
    }
}