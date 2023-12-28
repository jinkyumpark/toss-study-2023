package controller

import http.HttpRequest
import http.HttpResponse

class GetIndexController : Controller {
    override fun process(request: HttpRequest): HttpResponse {
        return GetResourceController().process(
            HttpRequest(
                method = HttpRequest.Method.GET,
                url = HttpRequest.Url(
                    base = "/index.html",
                    parameters = mapOf()
                ),
                headers = request.headers,
                cookie = request.cookie,
                body = request.body,
            )
        )
    }
}
