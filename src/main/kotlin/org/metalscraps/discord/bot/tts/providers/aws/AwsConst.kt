package org.metalscraps.discord.bot.tts.providers.aws

internal object AwsConst {
    const val REQUEST_LINE = "POST /v1/speech"
    const val HOST = "https://polly.ap-northeast-2.amazonaws.com"
    const val KEY_PROPERTY_NAME = "AWS_TTS_KEY"
    const val SECRET_PROPERTY_NAME = "AWS_TTS_SECRET"
}