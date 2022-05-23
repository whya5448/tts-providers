package org.metalscraps.discord.tts.providers.random

import org.jetbrains.annotations.NotNull
import org.metalscraps.discord.bot.tts.core.DefaultTTSService
import org.metalscraps.discord.bot.tts.core.Response
import org.metalscraps.discord.bot.tts.core.TTSProvider
import org.metalscraps.discord.bot.tts.core.Voice

class RandomTTSService(private val proxy: DefaultTTSService) : TTSProvider {
    private val voice = RandomVoice(this)

    companion object {
        internal const val ID: String = "random"
    }

    override fun getId(): String {
        return ID
    }

    override fun getFriendlyName(): String {
        return "랜덤"
    }

    @NotNull
    override fun synthesize(voice: String, text: String): Response {
        val random = getRandomVoice().random()
        return proxy.synthesize(random.getProvider(), random.getId(), text)
    }

    override fun getVoices(): List<Voice> {
        return listOf(voice)
    }

    internal fun getRandomVoice(): List<Voice> {
        return proxy.getProviders().flatMap { it.value.getVoices() }.filter { it != voice }
    }

}