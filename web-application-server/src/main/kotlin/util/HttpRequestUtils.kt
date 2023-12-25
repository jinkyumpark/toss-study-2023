package util

import java.io.InputStream

object HttpRequestUtils {

    fun parseUrl(inputStream: InputStream): String {
        return inputStream
            .bufferedReader()
            .readLine() // GET /index.html HTTP/1.1
            .split(" ")
            .drop(1)
            .first()
    }

    fun parseQueryString(queryString: String?): Map<String, String> {
        return parseValues(queryString, "&")
    }

    fun parseCookies(cookie: String): Map<String, String> {
        return parseValues(cookie, ";")
    }

    fun parseHeader(header: String): Pairs? {
        return getKeyValue(header, ": ")
    }

    fun getKeyValue(
        keyValue: String?,
        regex: String,
    ): Pairs? {
        if (keyValue.isNullOrEmpty()) {
            return null
        }

        val tokens = keyValue.split(regex)
        if (tokens.size != 2) {
            return null
        }

        return Pairs(
            key = tokens.first(),
            value = tokens.last(),
        )
    }

    private fun parseValues(
        values: String?,
        separator: String,
    ): Map<String, String> {
        if (values.isNullOrEmpty()) {
            return emptyMap()
        }

        val tokens = values.split(separator)
        return tokens
            .mapNotNull { getKeyValue(it, "=") }
            .associate { it.key to it.value }
    }

    data class Pairs(
        val key: String,
        val value: String,
    )
}
