package org.metalscraps.discord.tts.providers.azure

import feign.Feign
import feign.jaxb.JAXBContextFactory
import feign.jaxb.JAXBEncoder
import feign.slf4j.Slf4jLogger
import org.metalscraps.discord.tts.core.AudioFormat
import org.metalscraps.discord.tts.core.Response
import org.metalscraps.discord.tts.core.TTSProvider
import org.metalscraps.discord.tts.core.Voice
import org.metalscraps.discord.tts.providers.SSMLRequest
import org.metalscraps.discord.tts.providers.getProperty

class AzureTTSService(
    subscriptionKey: String? = getProperty(AzureConst.KEY_PROPERTY_NAME),
) : TTSProvider {
    companion object {
        internal const val ID: String = "azure"
    }

    private val client: AzureClient
    private val subscriptionKey: String

    init {
        require(!subscriptionKey.isNullOrBlank())
        this.subscriptionKey = subscriptionKey

        val jaxbFactory = JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding("UTF-8")
            .withMarshallerSchemaLocation(org.metalscraps.discord.tts.providers.Const.SSML_SCHEMA)
            .build()

        this.client = Feign.builder()
            .logger(Slf4jLogger())
            .logLevel(org.metalscraps.discord.tts.providers.Const.LOG_LEVEL)
            .encoder(JAXBEncoder(jaxbFactory))
            .target(AzureClient::class.java, AzureConst.HOST)
    }

    override fun getId(): String {
        return ID
    }

    override fun getFriendlyName(): String {
        return "마이크로소프트"
    }

    override fun synthesize(voice: String, text: String): Response {
        val response = client.synthesize(subscriptionKey, SSMLRequest(voice, text))
        val status = response.status()

        if (status != 200) {
            return Response.error("$status $response")
        }

        val responseBody: ByteArray = try {
            response.body().asInputStream().readBytes()
        } catch (e: Exception) {
            return Response.error("$e")
        }

        return Response.data(AudioFormat.OGG, responseBody)
    }

    override fun getVoices(): List<Voice> {
        return AzureVoice.values().asList()
    }
}