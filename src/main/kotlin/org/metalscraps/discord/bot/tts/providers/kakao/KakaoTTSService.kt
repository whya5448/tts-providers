package org.metalscraps.discord.bot.tts.providers.kakao

import feign.Feign
import feign.jaxb.JAXBContextFactory
import feign.jaxb.JAXBEncoder
import feign.slf4j.Slf4jLogger
import org.metalscraps.discord.bot.tts.core.AudioFormat
import org.metalscraps.discord.bot.tts.core.Response
import org.metalscraps.discord.bot.tts.core.TTSProvider
import org.metalscraps.discord.bot.tts.core.Voice
import org.metalscraps.discord.bot.tts.providers.Const
import org.metalscraps.discord.bot.tts.providers.SSMLRequest
import org.metalscraps.discord.bot.tts.providers.getProperty

class KakaoTTSService(
    appKey: String? = getProperty(KakaoConst.KEY_PROPERTY_NAME)
) : TTSProvider {
    companion object {
        internal val ID: String = "kakao"
        @JvmStatic
        fun main(args: Array<String>) {
            val kakaoTTSService = KakaoTTSService()
            val synthesize = kakaoTTSService.synthesize(kakaoTTSService.getVoices().random().getId(), ".")
            println(synthesize)
        }
    }

    private val client: KakaoClient
    private val appKey: String

    init {
        require(!appKey.isNullOrBlank())
        this.appKey = appKey

        val jaxbFactory = JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .withMarshallerSchemaLocation(Const.SSML_SCHEMA)
            .build()

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(Const.LOG_LEVEL)
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
            return Response.error("$status $response")
        }

        val responseBody: ByteArray = try {
            response.body().asInputStream().readBytes()
        } catch (e: Exception) {
            return Response.error("$e")
        }

        return Response.data(AudioFormat.MP3, responseBody)
    }

    override fun getVoices(): List<Voice> {
        return KakaoVoice.values().asList()
    }
}