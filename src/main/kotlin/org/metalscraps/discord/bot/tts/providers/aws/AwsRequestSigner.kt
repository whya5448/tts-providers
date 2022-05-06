package org.metalscraps.discord.bot.tts.providers.aws

import feign.RequestInterceptor
import feign.RequestTemplate
import org.metalscraps.discord.bot.tts.providers.aws.util.AwsBinary
import org.metalscraps.discord.bot.tts.providers.aws.util.AwsHash
import org.metalscraps.discord.bot.tts.providers.aws.util.AwsSigning
import org.metalscraps.discord.bot.tts.providers.aws.util.AwsSigning.AWS4_SIGNING_ALGORITHM
import java.net.URI
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AwsRequestSigner(
    private val region: String = "ap-northeast-2",
    private val accessKeyId: String,
    private val secretAccessKey: String
) : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        val region = region
        val service = "polly"
        val host = "$service.$region.amazonaws.com"

        val timestampFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        val now = ZonedDateTime.now(ZoneOffset.UTC)
        val timestamp = now.format(timestampFormatter)
        val date = now.format(dateFormatter)

        val contentHash = AwsBinary.toHex(AwsHash.hash(template.body()))

        template.target("https://$host")
        template.header("X-Amz-Date", timestamp)
        template.header("Host", host)
        val headers = template.headers().map { it.key.lowercase() to it.value.joinToString(",") }
            .sortedBy { it.first }

        val signedHeadersString = headers.joinToString(";") { it.first }

        val canonicalRequest = template.method() +
                "\n" +
                URI(template.path()).path +
                "\n" +
                template.queryLine() +
                "\n" +
                headers.joinToString("\n") { "${it.first}:${it.second}" } +
                "\n" +
                "\n" +
                signedHeadersString +
                "\n" +
                contentHash

        val scope = "$date/$region/$service/aws4_request"

        val stringToSign = AWS4_SIGNING_ALGORITHM +
                "\n" +
                timestamp +
                "\n" +
                scope +
                "\n" +
                AwsBinary.toHex(AwsHash.hash(canonicalRequest))

        val signature = AwsSigning.sign(
            stringToSign,
            AwsSigning.getSigningKey(date, secretAccessKey, region, service)
        )

        template.header(
            "Authorization",
            AwsSigning.buildAuthorizationHeader(signature, accessKeyId, scope, signedHeadersString)
        )
    }
}