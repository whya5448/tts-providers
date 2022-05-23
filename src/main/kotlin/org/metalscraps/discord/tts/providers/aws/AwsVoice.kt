package org.metalscraps.discord.tts.providers.aws

import org.metalscraps.discord.bot.tts.core.Voice

enum class AwsVoice(
    private val id: String,
    private val friendlyName: String
) : Voice {
    Seoyeon("Seoyeon", "서연");

    override fun getProvider(): String {
        return AwsTTSService.ID
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
        fun getRandomVoice(): AwsVoice {
            return values().random()
        }
    }
}