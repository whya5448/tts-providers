package org.metalscraps.discord.bot.tts.providers.google

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.slf4j.Slf4jLogger
import org.metalscraps.discord.bot.tts.core.AudioFormat
import org.metalscraps.discord.bot.tts.core.Response
import org.metalscraps.discord.bot.tts.core.TTSProvider
import org.metalscraps.discord.bot.tts.core.Voice
import org.metalscraps.discord.bot.tts.providers.Const
import org.metalscraps.discord.bot.tts.providers.getProperty
import java.util.*

class GoogleTTSService(
    apiKey: String? = getProperty(GoogleConst.KEY_PROPERTY_NAME)
) : TTSProvider {
    companion object {
        internal val ID: String = "google"
    }

    private val client: GoogleClient
    private val apiKey: String

    init {
        require(!apiKey.isNullOrBlank())
        this.apiKey = apiKey

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(Const.LOG_LEVEL)
            .encoder(JacksonEncoder())
            .decoder(JacksonDecoder())
            .target(GoogleClient::class.java, GoogleConst.HOST)
    }

    override fun getId(): String {
        return ID
    }

    override fun getFriendlyName(): String {
        return "구글"
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(GoogleApiKey(apiKey), GoogleRequest(voice, text))
        val status = response.status()

        if (status != 200) {
            return Response.error("$status $response")
        }

        val responseJson = response.body().asInputStream().readAllBytes().decodeToString()
        jacksonObjectMapper().readValue(responseJson, GoogleResponse::class.java).audioContent.run {
            if (!isNullOrBlank()) return Response.data(
                AudioFormat.OGG,
                Base64.getDecoder().decode(this)
            )
        }

        return Response.error("$status $response")
    }

    override fun getVoices(): List<Voice> {
        return GoogleVoice.values().asList()
    }
}