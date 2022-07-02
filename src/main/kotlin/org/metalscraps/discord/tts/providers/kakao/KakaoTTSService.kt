package org.metalscraps.discord.tts.providers.kakao

import feign.Feign
import feign.jaxb.JAXBContextFactory
import feign.jaxb.JAXBEncoder
import feign.slf4j.Slf4jLogger
import org.metalscraps.discord.tts.core.AudioFormat
import org.metalscraps.discord.tts.core.Response
import org.metalscraps.discord.tts.core.TTSProvider
import org.metalscraps.discord.tts.core.Voice
import org.metalscraps.discord.tts.providers.SSMLRequest
import org.metalscraps.discord.tts.providers.createResponseError
import org.metalscraps.discord.tts.providers.getProperty
import org.slf4j.LoggerFactory

class KakaoTTSService(
    appKey: String? = getProperty(KakaoConst.KEY_PROPERTY_NAME)
) : TTSProvider {
    companion object {
        internal const val ID: String = "kakao"
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val client: KakaoClient
    private val appKey: String

    init {
        require(!appKey.isNullOrBlank())
        this.appKey = appKey

        val jaxbFactory = JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .withMarshallerSchemaLocation(org.metalscraps.discord.tts.providers.Const.SSML_SCHEMA)
            .build()

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(org.metalscraps.discord.tts.providers.Const.LOG_LEVEL)
            .encoder(JAXBEncoder(jaxbFactory))
            .target(KakaoClient::class.java, KakaoConst.HOST)
    }

    override fun getId(): String {
        return ID
    }

    override fun getFriendlyName(): String {
        return "카카오"
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(appKey, SSMLRequest(voice, text))
        val status = response.status()

        if (status == 204) {
            return Response.data(AudioFormat.NONE, byteArrayOf())
        }

        if (status != 200) {
            return createResponseError(response)
        }

        val responseBody: ByteArray = try {
            response.body().asInputStream().readBytes()
        } catch (e: Exception) {
            return createResponseError(response, e.message.toString(), e.stackTraceToString())
        }

        return Response.data(AudioFormat.MP3, responseBody)
    }

    override fun getVoices(): List<Voice> {
        return KakaoVoice.values().asList()
    }
}