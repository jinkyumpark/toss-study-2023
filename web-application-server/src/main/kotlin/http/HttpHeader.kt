package http

data class HttpHeader(
    val key: String,
    val value: String,
) {
    enum class Key(
        val raw: String,
    ) {
        REDIRECT_URL("Location"),
        CONTENT_TYPE("Content-Type"),
        CONTENT_LENGTH("Content-Length"),
        SET_COOKIE("Set-Cookie"),
        COOKIE("Cookie"),
    }

    class Value {
        enum class ContentType(
            val raw: String,
        ) {
            HTML("text/html")
        }
    }
}
