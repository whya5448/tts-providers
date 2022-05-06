package org.metalscraps.discord.bot.tts.providers.azure

import org.metalscraps.discord.bot.tts.core.Voice

enum class AzureVoice(
    private val id: String,
    private val friendlyName: String
) : Voice {
    Sun_Hi("ko-KR-SunHiNeural", "선희"),
    InJoon("ko-KR-InJoonNeural", "인준");

    override fun getProvider(): String {
        return AzureTTSService.ID
    }

    override fun getId(): String {
        return id
    }

    override fun getFriendlyName(): String {
        return friendlyName
    }

    override fun getRandomVoice(): Voice {
        return Companion.getRandomVoice()
    }

    companion object {
        fun getRandomVoice(): AzureVoice {
            return values().random()
        }
    }
}