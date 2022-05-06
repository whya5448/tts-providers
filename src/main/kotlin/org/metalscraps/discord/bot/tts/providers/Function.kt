package org.metalscraps.discord.bot.tts.providers

fun getProperty(name: String): String? {
    val property = System.getProperty(name)
    if (property != null) return property

    return System.getenv(name)
}

fun getProperty(name: String, default: String): String {
    return getProperty(name) ?: default
}