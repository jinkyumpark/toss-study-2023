package model

enum class HttpHeader(
    val key: String,
) {
    REDIRECT_URL("Location"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    COOKIE("Set-Cookie"),
}