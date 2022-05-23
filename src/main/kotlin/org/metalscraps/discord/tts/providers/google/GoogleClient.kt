package org.metalscraps.discord.tts.providers.google

import feign.Headers
import feign.QueryMap
import feign.RequestLine
import feign.Response

// https://cloud.google.com/text-to-speech/docs
interface GoogleClient {
    @RequestLine(GoogleConst.REQUEST_LINE)
    @Headers("Content-type: application/json")
    fun synthesize(
        @QueryMap apiKey: GoogleApiKey,
        jsonRequest: GoogleRequest
    ): Response
}