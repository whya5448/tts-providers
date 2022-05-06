package org.metalscraps.discord.bot.tts.providers.random

import org.metalscraps.discord.bot.tts.core.Voice

class RandomVoice(private val proxy: RandomTTSService) : Voice {
    override fun getId(): String {
        return "random"
    }

    override fun getFriendlyName(): String {
        return "랜덤"
    }

    override fun getRandomVoice(): Voice {
        return proxy.getRandomVoice().random()
    }

    override fun getProvider(): String {
        return RandomTTSService.ID
    }
}