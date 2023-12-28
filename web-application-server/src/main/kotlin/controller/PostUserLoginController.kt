package controller

import db.UserRepository
import exception.ResponseStatusException
import http.*

class PostUserLoginController : Controller {
    override fun process(request: HttpRequest): HttpResponse {
        val userId = request.body["userId"] ?: throw ResponseStatusException(HttpResponse.Status.BAD_REQUEST)
        val password = request.body["password"] ?: throw ResponseStatusException(HttpResponse.Status.BAD_REQUEST)

        val user = UserRepository.findById(userId)
        val responseHeaders = when {
            user?.password == password -> {
                listOf(
                    HttpHeader(
                        key = HttpHeader.Key.REDIRECT_URL.raw,
                        value = "/index.html",
                    ),
                    HttpHeader(
                        key = HttpHeader.Key.SET_COOKIE.raw,
                        value = "isLogin=true;",
                    ),
                )
            }

            else -> {
                listOf(
                    HttpHeader(
                        key = HttpHeader.Key.REDIRECT_URL.raw,
                        value = "/user/login_failed.html",
                    ),
                    HttpHeader(
                        key = HttpHeader.Key.SET_COOKIE.raw,
                        value = "isLogin=false;",
                    ),
                )
            }
        }

        return HttpResponse(
            headers = responseHeaders,
            status = HttpResponse.Status.REDIRECT,
            body = HttpResponse.Body(
                contentType = HttpHeader.Value.ContentType.HTML,
                data = byteArrayOf(),
            )
        )
    }
}