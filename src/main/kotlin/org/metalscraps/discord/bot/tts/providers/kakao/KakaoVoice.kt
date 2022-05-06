package org.metalscraps.discord.bot.tts.providers.kakao

import org.metalscraps.discord.bot.tts.core.Voice

enum class KakaoVoice(
    private val id: String,
    private val friendlyName: String
) : Voice {
    WOMAN_READ_CALM("WOMAN_READ_CALM", "카카오녀차분"),
    MAN_READ_CALM("MAN_READ_CALM", "카카오남차분"),
    WOMAN_DIALOG_BRIGHT("WOMAN_DIALOG_BRIGHT", "카카오녀활발"),
    MAN_DIALOG_BRIGHT("MAN_DIALOG_BRIGHT", "카카오남활발");

    override fun getProvider(): String {
        return KakaoTTSService.ID
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
        fun getRandomVoice(): KakaoVoice {
            return values().random()
        }
    }

    enum class Prosody {
        FAST, SLOW
    }
}