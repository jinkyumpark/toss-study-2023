package http

data class HttpRequest(
    val method: Method,
    val url: Url,
    val headers: List<HttpHeader>,
    val cookie: Map<String, String>,
    val body: Map<String, String>,
) {
    data class Url(
        val base: String,
        val parameters: Map<String, String>,
    )

    enum class Method {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        HEAD,
        CONNECT,
        OPTION,
        TRACE,
    }
}