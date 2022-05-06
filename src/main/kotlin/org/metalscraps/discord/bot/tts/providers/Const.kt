package org.metalscraps.discord.bot.tts.providers

import feign.Logger

object Const {
    const val SSML_SCHEMA = "https://www.w3.org/TR/speech-synthesis11/synthesis-nonamespace.xsd"
    val LOG_LEVEL: Logger.Level = Logger.Level.FULL
}