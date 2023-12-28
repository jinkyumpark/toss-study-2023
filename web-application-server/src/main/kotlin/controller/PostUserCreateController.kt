package controller

import db.UserRepository
import exception.ResponseStatusException
import http.*
import model.User

class PostUserCreateController : Controller {
    override fun process(request: HttpRequest): HttpResponse {
        val userId = request.body["userId"] ?: throw ResponseStatusException(HttpResponse.Status.BAD_REQUEST)
        val password = request.body["password"] ?: throw ResponseStatusException(HttpResponse.Status.BAD_REQUEST)
        val name = request.body["name"] ?: throw ResponseStatusException(HttpResponse.Status.BAD_REQUEST)

        val user = User(
            userId = userId,
            password = password,
            name = name,
            email = null,
        )
        UserRepository.add(user)

        val responseHeaders = listOf(
            HttpHeader(
                key = HttpHeader.Key.REDIRECT_URL.raw,
                value = "/index.html",
            ),
        )

        return HttpResponse(
            headers = responseHeaders,
            status = HttpResponse.Status.REDIRECT,
            body = HttpResponse.Body(
                contentType = HttpHeader.Value.ContentType.HTML,
                data = byteArrayOf(),
            ),
        )
    }
}
