package org.metalscraps.discord.bot.tts.providers.aws

import feign.Headers
import feign.Param
import feign.RequestLine
import feign.Response
import org.metalscraps.discord.bot.tts.providers.SSMLRequest

// https://docs.aws.amazon.com/polly/latest/dg/API_SynthesizeSpeech.html
internal interface AwsClient {
    @RequestLine(AwsConst.REQUEST_LINE)
    @Headers("Content-Type: application/json")
    fun synthesize(
        jsonRequest: AwsRequest
    ): Response
}