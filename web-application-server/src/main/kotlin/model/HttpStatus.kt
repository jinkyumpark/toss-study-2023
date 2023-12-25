package model

enum class HttpStatus(
    val code: Int,
    val displayName: String,
) {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
}
