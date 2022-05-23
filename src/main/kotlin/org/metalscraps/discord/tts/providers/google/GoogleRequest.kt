package org.metalscraps.discord.tts.providers.google

import com.fasterxml.jackson.annotation.JsonProperty

class GoogleRequest(voice: String, text: String) {
    @JsonProperty
    private val input: Input

    @JsonProperty
    private val voice: Voice

    @JsonProperty
    private val audioConfig = AudioConfig()

    init {
        this.input = Input(text)
        this.voice = Voice(name = voice)
    }

}

private data class Input(val text: String)

private data class Voice(
    val languageCode: String = "ko-KR",
    val name: String
)

private data class AudioConfig(val audioEncoding: String = "OGG_OPUS")
