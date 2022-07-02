package org.metalscraps.discord.tts.providers

import feign.Response
import feign.Util

fun getProperty(name: String): String? {
    val property = System.getProperty(name)
    if (property != null) return property

    return System.getenv(name)
}

fun getProperty(name: String, default: String): String {
    return getProperty(name) ?: default
}

fun createResponseError(response: Response, vararg string: String): org.metalscraps.discord.tts.core.Response {
    response.run {
        val builder = StringBuilder("HTTP/1.1 ").append(status())
        if (reason() != null) builder.append(' ').append(reason())
        builder.append('\n')
        for (field in headers().keys) {
            for (value in Util.valuesOrEmpty(headers(), field)) {
                builder.append(field).append(": ").append(value).append('\n')
            }
        }

        body()?.let {
            (it.asInputStream()?.readAllBytes() ?: ByteArray(0)).let {
                builder.append('\n').append(String(it))
            }
        }

        builder.append('\n')

        if (string.isNotEmpty()) {
            builder.append('\n').append(string)
        }

        return org.metalscraps.discord.tts.core.Response.error(builder.toString())
    }
}