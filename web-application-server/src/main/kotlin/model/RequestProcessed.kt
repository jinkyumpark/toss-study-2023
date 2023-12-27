package model

data class RequestProcessed(
    val headers: Map<HttpHeader, String>,
    val status: HttpStatus,
    val body: Body,
) {
    data class Body(
        val contentType: HttpContentType,
        val data: ByteArray,
    )
}
