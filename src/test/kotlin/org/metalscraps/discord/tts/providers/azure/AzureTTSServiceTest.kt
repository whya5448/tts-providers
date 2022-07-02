package org.metalscraps.discord.tts.providers.azure

import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.metalscraps.discord.tts.providers.azure.AzureConst.KEY_PROPERTY_NAME
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
internal class AzureTTSServiceTest {

    @Test
    fun synthesize() {
        assertThrows<IllegalArgumentException> { AzureTTSService() }

        let {
            withEnvironmentVariable(KEY_PROPERTY_NAME, "123").execute { AzureTTSService() }
            assertThrows<IllegalArgumentException> { AzureTTSService() }
        }

        let {
            val properties = System.getProperties()
            properties.setProperty(KEY_PROPERTY_NAME, "123")
            AzureTTSService()
            properties.remove(KEY_PROPERTY_NAME)
            assertThrows<IllegalArgumentException> { AzureTTSService() }
        }

        let {
            val azureTTSService = AzureTTSService("_")
            val synthesize = azureTTSService.synthesize("Hello World!")
            assertTrue(synthesize.error)
            assertEquals("HTTP/1.1 401 Unauthorized", synthesize.errorMessage.lines()[0])
        }

        let {
            val azureTTSService = AzureTTSService("_")
            val synthesize = azureTTSService.synthesize("", "Hello World!")
            assertTrue(synthesize.error)
            assertEquals("HTTP/1.1 401 Unauthorized", synthesize.errorMessage.lines()[0])
        }
    }

    @Test
    fun getId() {
        assertEquals("azure", AzureTTSService("_").getId())
    }

    @Test
    fun getFriendlyName() {
        assertEquals("마이크로소프트", AzureTTSService("_").getFriendlyName())
    }

    @Test
    fun getVoices() {
        val voices = AzureTTSService("_").getVoices()
        assertTrue(AzureVoice.values().asList().containsAll(voices))
    }
}