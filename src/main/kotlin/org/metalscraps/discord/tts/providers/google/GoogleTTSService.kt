package org.metalscraps.discord.tts.providers.google

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import feign.slf4j.Slf4jLogger
import org.metalscraps.discord.tts.core.AudioFormat
import org.metalscraps.discord.tts.core.Response
import org.metalscraps.discord.tts.core.TTSProvider
import org.metalscraps.discord.tts.core.Voice
import org.metalscraps.discord.tts.providers.createResponseError
import org.metalscraps.discord.tts.providers.getProperty
import org.slf4j.LoggerFactory
import java.util.*

class GoogleTTSService(
    apiKey: String? = getProperty(GoogleConst.KEY_PROPERTY_NAME)
) : TTSProvider {
    companion object {
        internal const val ID: String = "google"
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val client: GoogleClient
    private val apiKey: String

    init {
        require(!apiKey.isNullOrBlank())
        this.apiKey = apiKey

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(org.metalscraps.discord.tts.providers.Const.LOG_LEVEL)
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
            return createResponseError(response)
        }

        val responseJson = response.body().asInputStream().readAllBytes().decodeToString()

        val googleResponse = jacksonObjectMapper().readValue(responseJson, GoogleResponse::class.java)

        if (googleResponse.audioContent.isNullOrBlank()) {
            return createResponseError(response, "$googleResponse")
        }

        return googleResponse.audioContent.run {
            Response.data(
                AudioFormat.OGG,
                Base64.getDecoder().decode(this)
            )
        }
    }

    override fun getVoices(): List<Voice> {
        return GoogleVoice.values().asList()
    }
}