package http

data class HttpResponse(
    val headers: List<HttpHeader>,
    val status: Status,
    val body: Body,
) {
    data class Body(
        val contentType: HttpHeader.Value.ContentType,
        val data: ByteArray,
    )

    enum class Status(
        val code: Int,
        val displayName: String,
    ) {
        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        BAD_REQUEST(400, "Bad Request"),
        REDIRECT(302, "Found"),
    }
}
