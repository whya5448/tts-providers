module metalscraps.tts.providers {
    requires metalscraps.tts.core;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.kotlin;
    requires java.xml.bind;

    exports org.metalscraps.discord.tts.providers;
}