package service

import db.UserRepository
import exception.ResponseStatusException
import model.*

object PostUserLoginRequestService : RequestService {
    override fun process(
        method: HttpMethod?,
        url: String?,
        headers: Map<String, String>,
        body: Map<String, String>,
        cookie: Map<String, String>,
        urlParameters: Map<String, String>
    ): RequestProcessed {
        val userId = body["userId"] ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        val password = body["password"] ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

        val user = UserRepository.findById(userId)
        val responseHeaders = when {
            user?.password == password -> {
                mapOf(
                    HttpHeader.REDIRECT_URL to "/index.html",
                    HttpHeader.COOKIE to "isLogin=true;",
                )
            }

            else -> {
                mapOf(
                    HttpHeader.REDIRECT_URL to "/user/login_failed.html",
                    HttpHeader.COOKIE to "isLogin=false;",
                )
            }
        }

        return RequestProcessed(
            headers = responseHeaders,
            status = HttpStatus.REDIRECT,
            body = RequestProcessed.Body(
                contentType = HttpContentType.HTML,
                data = byteArrayOf(),
            )
        )
    }
}