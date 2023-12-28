package controller

import db.UserRepository
import http.*

class GetUserListController : Controller {
    override fun process(request: HttpRequest): HttpResponse {
        val isLogin = when {
            request.cookie["isLogin"] == "true" -> true
            else -> false
        }

        if (!isLogin) {
            return HttpResponse(
                headers = listOf(
                    HttpHeader(
                        key = HttpHeader.Key.REDIRECT_URL.raw,
                        value = "/user/login.html"
                    ),
                ),
                body = HttpResponse.Body(
                    contentType = HttpHeader.Value.ContentType.HTML,
                    data = byteArrayOf(),
                ),
                status = HttpResponse.Status.REDIRECT,
            )
        }

        val responseBody = UserRepository
            .findAll()
            .joinToString("") {
                """
                    <li>
                        ${it.userId} (${it.email})
                        <br/>
                        name: ${it.name}
                        <br/>
                        password: ${it.password}
                    </li>
                """.trimIndent()
            }
            .let {
                """
                <!DOCTYPE html>
                <html>
                    <body>
                        <ol>$it</ol>
                    </body>
                <!html>
            """.trimIndent()
            }
            .toByteArray()

        return HttpResponse(
            headers = listOf(),
            status = HttpResponse.Status.OK,
            body = HttpResponse.Body(
                contentType = HttpHeader.Value.ContentType.HTML,
                data = responseBody,
            )
        )
    }
}