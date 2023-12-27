package util

import model.HttpMethod

object HttpRequestUtils {

    fun parseUrl(headers: String): String {
        return headers
            .split("\r\n")
            .first()
            .split(" ")
            .drop(1)
            .first()
    }

    fun parseMethod(headers: String): HttpMethod {
        val raw = headers
            .split("\r\n")
            .first()
            .split(" ")
            .first()

        return HttpMethod.valueOf(raw)
    }

    fun parseHeaders(headers: String): Map<String, String> {
        return headers
            .split("\r\n")
            .drop(1)
            .dropLast(2) // drop body and separator
            .map { it.split(": ") }
            .associate { it.first() to it.last() }
    }

    fun parseBody(body: String): Map<String, String> {
        return body
            .split("&")
            .map { it.split("=") }
            .filter { it.size == 2 }
            .associate { it.first() to it.last() }
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

    private fun getKeyValue(
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
