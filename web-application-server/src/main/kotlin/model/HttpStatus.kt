package model

enum class HttpStatus(
    val code: Int,
    val displayName: String,
) {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    BAD_REQUEST(400, "Bad Request"),
    REDIRECT(302, "Found"),
}
