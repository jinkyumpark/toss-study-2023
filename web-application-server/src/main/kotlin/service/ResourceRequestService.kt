package service

import exception.ResponseStatusException
import model.HttpMethod
import model.HttpStatus
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
    ): ByteArray {
        return try {
            Files.readAllBytes(Path.of("./webapp$url"))
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }
}
