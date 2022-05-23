package org.metalscraps.discord.tts.providers.google

import org.metalscraps.discord.bot.tts.core.Voice

enum class GoogleVoice(
    private val id: String,
    private val friendlyName: String
) : Voice {
    ko_KR_Standard_A("ko-KR-Standard-A", "구글녀1"),
    ko_KR_Standard_B("ko-KR-Standard-B", "구글녀2"),
    ko_KR_Standard_C("ko-KR-Standard-C", "구글남1"),
    ko_KR_Standard_D("ko-KR-Standard-D", "구글남2");

    override fun getProvider(): String {
        return GoogleTTSService.ID
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
        fun getRandomVoice(): GoogleVoice {
            return values().random()
        }
    }
}