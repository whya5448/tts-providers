package org.metalscraps.discord.tts.providers.kakao

import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.metalscraps.discord.tts.providers.kakao.KakaoConst.KEY_PROPERTY_NAME
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class KakaoTTSServiceTest {

    @Test
    fun synthesize() {
        assertThrows<IllegalArgumentException> { KakaoTTSService() }

        let {
            withEnvironmentVariable(KEY_PROPERTY_NAME, "123").execute { KakaoTTSService() }
            assertThrows<IllegalArgumentException> { KakaoTTSService() }
        }

        let {
            val properties = System.getProperties()
            properties.setProperty(KEY_PROPERTY_NAME, "123")
            KakaoTTSService()
            properties.remove(KEY_PROPERTY_NAME)
            assertThrows<IllegalArgumentException> { KakaoTTSService() }
        }

        let {
            val kakaoTTSService = KakaoTTSService("123")
            val synthesize = kakaoTTSService.synthesize(".")
            assertTrue(synthesize.error)
            assertEquals("401 HTTP/1.1 401 Unauthorized", synthesize.errorMessage.lines()[0])
        }

        let {
            val kakaoTTSService = KakaoTTSService("123")
            val synthesize = kakaoTTSService.synthesize("", ".")
            assertTrue(synthesize.error)
            assertEquals("401 HTTP/1.1 401 Unauthorized", synthesize.errorMessage.lines()[0])
        }
    }
}