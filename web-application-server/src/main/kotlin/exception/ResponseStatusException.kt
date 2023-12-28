package exception

import http.HttpResponse

class ResponseStatusException(
    val status: HttpResponse.Status,
) : IllegalStateException()
