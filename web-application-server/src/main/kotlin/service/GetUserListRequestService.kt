package service

import db.UserRepository
import model.*

object GetUserListRequestService : RequestService {
    override fun process(
        method: HttpMethod?,
        url: String?,
        headers: Map<String, String>,
        body: Map<String, String>,
        cookie: Map<String, String>,
        urlParameters: Map<String, String>
    ): RequestProcessed {
        val isLogin = when {
            cookie["isLogin"] == "true" -> true
            else -> false
        }

        if (!isLogin) {
            return RequestProcessed(
                headers = mapOf(
                    HttpHeader.REDIRECT_URL to "/user/login.html"
                ), status = HttpStatus.REDIRECT, body = RequestProcessed.Body(
                    contentType = HttpContentType.HTML,
                    data = byteArrayOf(),
                )
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

        return RequestProcessed(
            headers = mapOf(),
            status = HttpStatus.OK,
            body = RequestProcessed.Body(
                contentType = HttpContentType.HTML,
                data = responseBody,
            )
        )
    }
}