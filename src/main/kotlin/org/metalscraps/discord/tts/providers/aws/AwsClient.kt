package org.metalscraps.discord.tts.providers.aws

import feign.Headers
import feign.RequestLine
import feign.Response

// https://docs.aws.amazon.com/polly/latest/dg/API_SynthesizeSpeech.html
internal interface AwsClient {
    @RequestLine(AwsConst.REQUEST_LINE)
    @Headers("Content-Type: application/json")
    fun synthesize(
        jsonRequest: AwsRequest
    ): Response
}