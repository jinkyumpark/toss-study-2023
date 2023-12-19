package util

object HttpRequestUtils {

    fun parseQueryString(queryString: String?): Map<String, String> {
        return parseValues(queryString, "&")
    }

    fun parseCookies(cookie: String): Map<String, String> {
        return parseValues(cookie, ";")
    }

    fun parseHeader(
        header: String,
    ): Pairs? {
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
            .map { getKeyValue(it, "=") }
            .filterNotNull()
            .associate { it.key to it.value }
    }

    data class Pairs(
        val key: String,
        val value: String,
    )
}
