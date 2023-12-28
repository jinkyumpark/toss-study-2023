package util

import http.HttpHeader
import http.HttpRequest
import java.io.InputStream
import kotlin.text.StringBuilder

object HttpRequestUtils {

    fun getRequest(input: InputStream): HttpRequest {
        val reader = input.bufferedReader()

        val lines = mutableListOf<String>()
        var line = reader.readLine()

        while (line.isNotBlank()) {
            lines.add(line)
            line = reader.readLine()
        }

        val headersRaw = lines
            .takeWhile { it.isNotBlank() }
            .joinToString("\r\n")
        val headers = parseHeaders(headersRaw)

        val contentLength = headers
            .find { it.key == HttpHeader.Key.CONTENT_LENGTH.raw }
            ?.value
            ?.toInt()
            ?: 0

        val bodyRaw = when {
            contentLength > 0 -> {
                StringBuilder()
                    .apply {
                        repeat(contentLength) {
                            append(reader.read().toChar())
                        }
                    }
                    .toString()
            }

            else -> ""
        }
        val body = parseBody(bodyRaw)

        val method = parseMethod(headersRaw)
        val url = parseUrl(headersRaw)
        val urlParameters = parseUrlParameter(headersRaw)

        val cookie = parseCookies(
            headers
                .find { it.key == HttpHeader.Key.COOKIE.raw }
                ?.value
                ?: ""
        )

        return HttpRequest(
            method = method,
            url = HttpRequest.Url(
                base = url,
                parameters = urlParameters,
            ),
            headers = headers,
            cookie = cookie,
            body = body,
        )
    }

    private fun parseUrl(headers: String): String {
        return headers
            .split("\r\n")
            .first()
            .split(" ")
            .drop(1)
            .first()
    }

    private fun parseUrlParameter(headers: String): Map<String, String> {
        val urlPart = headers
            .split("\r\n")
            .firstOrNull()
            ?.split(" ")
            ?.getOrNull(1)
            ?: return emptyMap()

        val queryParams = urlPart.substringAfter("?", "")
        return when {
            queryParams.isEmpty() -> emptyMap()
            else -> {
                queryParams
                    .split("&")
                    .mapNotNull {
                        val parts = it.split("=")
                        if (parts.size == 2) parts.first() to parts.last() else null
                    }
                    .toMap()
            }
        }
    }

    private fun parseMethod(headers: String): HttpRequest.Method {
        val raw = headers
            .split("\r\n")
            .first()
            .split(" ")
            .first()

        return HttpRequest.Method.valueOf(raw)
    }

    private fun parseHeaders(headers: String): List<HttpHeader> {
        return headers
            .split("\r\n")
            .drop(1)
            .map { it.split(": ") }
            .map {
                HttpHeader(
                    key = it.first(),
                    value = it.last(),
                )
            }
    }

    private fun parseBody(body: String): Map<String, String> {
        return body
            .split("&")
            .map { it.split("=") }
            .filter { it.size == 2 }
            .associate { it.first() to it.last() }
    }

    private fun parseCookies(cookie: String): Map<String, String> {
        return cookie
            .split(";")
            .map { it.split("=") }
            .associate { it.first() to it.last() }
    }
}
