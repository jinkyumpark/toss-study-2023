package service

import model.HttpMethod
import model.RequestProcessed

interface RequestService {
    fun process(
        method: HttpMethod? = null,
        url: String? = null,
        headers: Map<String, String>,
        body: Map<String, String> = mapOf(),
        cookie: Map<String, String> = mapOf(),
        urlParameters: Map<String, String> = mapOf(),
    ): RequestProcessed
}
