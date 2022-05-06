package org.metalscraps.discord.bot.tts.providers

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue

@XmlRootElement(name = "speak")
class SSMLRequest constructor(name: String, text: String) {
    private constructor() : this("", "")

    @XmlAttribute(name = "xml:lang")
    val lang = "ko-KR"

    @XmlAttribute(name = "version")
    val version = "1.0"

    @XmlElement
    val voice = Text(name, text)

    class Text(
        @XmlAttribute
        val name: String,
        @XmlValue
        val text: String
    )
}