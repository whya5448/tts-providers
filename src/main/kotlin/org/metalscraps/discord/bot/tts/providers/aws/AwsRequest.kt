package org.metalscraps.discord.bot.tts.providers.aws

import com.fasterxml.jackson.annotation.JsonProperty

class AwsRequest(
    @field:JsonProperty("VoiceId")
    private val voiceId: String,
    @field:JsonProperty("Text")
    private val text: String
) {
    @JsonProperty("OutputFormat")
    private val audioConfig = "ogg_vorbis"
}