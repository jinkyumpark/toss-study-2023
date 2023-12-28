package controller

import exception.ResponseStatusException
import http.HttpHeader
import http.HttpRequest
import http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path

class GetResourceController : Controller {
    override fun process(request: HttpRequest): HttpResponse {
        val data = try {
            Files.readAllBytes(Path.of("./webapp${request.url.base}"))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpResponse.Status.NOT_FOUND)
        }

        return HttpResponse(
            headers = listOf(),
            status = HttpResponse.Status.OK,
            body = HttpResponse.Body(
                contentType = HttpHeader.Value.ContentType.HTML, // TODO - set contentType based on type of data
                data = data,
            )
        )
    }
}
