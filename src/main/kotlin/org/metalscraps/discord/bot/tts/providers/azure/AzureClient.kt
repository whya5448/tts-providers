package org.metalscraps.discord.bot.tts.providers.azure

import feign.Headers
import feign.Param
import feign.RequestLine
import feign.Response
import org.metalscraps.discord.bot.tts.providers.SSMLRequest

// https://docs.microsoft.com/ko-kr/azure/cognitive-services/speech-service/rest-text-to-speech
interface AzureClient {
    @RequestLine(AzureConst.REQUEST_LINE)
    @Headers(
        "Content-type: application/ssml+xml",
        "X-Microsoft-OutputFormat: ogg-24khz-16bit-mono-opus",
        "Ocp-Apim-Subscription-Key: {subscriptionKey}"
    )
    fun synthesize(
        @Param("subscriptionKey") subscriptionKey: String,
        ssmlRequest: SSMLRequest
    ): Response
}