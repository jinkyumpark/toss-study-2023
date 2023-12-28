package controller

import http.HttpRequest
import http.HttpResponse

interface Controller {
    fun  process(request: HttpRequest): HttpResponse
}
