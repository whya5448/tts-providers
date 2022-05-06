package org.metalscraps.discord.bot.tts.providers.kakao

import feign.Headers
import feign.Param
import feign.RequestLine
import feign.Response
import org.metalscraps.discord.bot.tts.providers.SSMLRequest


// https://developers.kakao.com/assets/guide/kakao_ssml_guide.pdf
internal interface KakaoClient {
    @RequestLine(KakaoConst.REQUEST_LINE)
    @Headers("Content-Type: application/xml", "Authorization: KakaoAK {appKey}")
    fun synthesize(
        @Param("appKey") appKey: String,
        ssmlRequest: SSMLRequest
    ): Response
}