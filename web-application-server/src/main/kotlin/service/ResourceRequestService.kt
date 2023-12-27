package service

import exception.ResponseStatusException
import model.HttpContentType
import model.HttpMethod
import model.HttpStatus
import model.RequestProcessed
import java.nio.file.Files
import java.nio.file.Path

object ResourceRequestService : RequestService {
    override fun process(
        method: HttpMethod?,
        url: String?,
        headers: Map<String, String>,
        body: Map<String, String>,
        cookie: Map<String, String>,
        urlParameters: Map<String, String>,
    ): RequestProcessed {
        val data = try {
            Files.readAllBytes(Path.of("./webapp$url"))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        return RequestProcessed(
            headers = mapOf(),
            status = HttpStatus.OK,
            body = RequestProcessed.Body(
                contentType = HttpContentType.HTML, // TODO - set contentType based on type of data
                data = data,
            )
        )
    }
}
