package org.metalscraps.discord.tts.providers.google

import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.metalscraps.discord.tts.providers.google.GoogleConst.KEY_PROPERTY_NAME
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class GoogleTTSServiceTest {

    @Test
    fun synthesize() {
        assertThrows<IllegalArgumentException> { GoogleTTSService() }

        let {
            withEnvironmentVariable(KEY_PROPERTY_NAME, "123").execute { GoogleTTSService() }
            assertThrows<IllegalArgumentException> { GoogleTTSService() }
        }

        let {
            val properties = System.getProperties()
            properties.setProperty(KEY_PROPERTY_NAME, "123")
            GoogleTTSService()
            properties.remove(KEY_PROPERTY_NAME)
            assertThrows<IllegalArgumentException> { GoogleTTSService() }
        }

        let {
            val googleTTSService = GoogleTTSService("_")
            val synthesize = googleTTSService.synthesize("Hello World!")
            assertTrue(synthesize.error)
            assertEquals("400 HTTP/1.1 400 Bad Request", synthesize.errorMessage.lines()[0])
        }

        let {
            val googleTTSService = GoogleTTSService("_")
            val synthesize = googleTTSService.synthesize("", "Hello World!")
            assertTrue(synthesize.error)
            assertEquals("400 HTTP/1.1 400 Bad Request", synthesize.errorMessage.lines()[0])
        }
    }

    @Test
    fun getId() {
        assertEquals("google", GoogleTTSService("_").getId())
    }

    @Test
    fun getFriendlyName() {
        assertEquals("구글", GoogleTTSService("_").getFriendlyName())
    }

    @Test
    fun getVoices() {
        val voices = GoogleTTSService("_").getVoices()
        assertTrue(GoogleVoice.values().asList().containsAll(voices))
    }
}