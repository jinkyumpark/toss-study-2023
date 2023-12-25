package webserver

import db.UserRepository
import exception.ResponseStatusException
import model.HttpContentType
import model.HttpStatus
import model.User
import org.slf4j.LoggerFactory
import util.HttpRequestUtils
import util.HttpResponseUtils
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import java.nio.file.Files
import java.nio.file.Path

class RequestHandler(
    private val connection: Socket,
) : Thread() {

    override fun run() {
        log.debug("New Client Connect! Connected IP : ${connection.getInetAddress()}, Port : ${connection.getPort()}")

        val input = connection.getInputStream()
        val output = connection.getOutputStream()

        try {
            val url = HttpRequestUtils.parseUrl(input)

            val body = when {
                url.startsWith("/user/create") -> {
                    val queries = HttpRequestUtils.parseQueryString(url.split("?").last())
                    val userId = queries["userId"] ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
                    val password = queries["password"] ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
                    val name = queries["name"] ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)

                    val user = User(
                        userId = userId,
                        password = password,
                        name = name,
                        email = null,
                    )

                    UserRepository.add(user)

                    byteArrayOf()
                }

                else -> {
                    try {
                        Files.readAllBytes(Path.of("./webapp$url"))
                    } catch (e: Exception) {
                        throw ResponseStatusException(HttpStatus.NOT_FOUND)
                    }
                }
            }

            val dataOutputStream = DataOutputStream(output)
            HttpResponseUtils.applyHttpHeader(
                dos = dataOutputStream,
                status = HttpStatus.OK,
                contentType = HttpContentType.HTML,
                bodyLength = body.size,
            )
            HttpResponseUtils.applyHttpBody(
                dos = dataOutputStream,
                body = body,
            )
        } catch (e: ResponseStatusException) {
            val dataOutputStream = DataOutputStream(output)
            HttpResponseUtils.applyHttpHeader(
                dos = dataOutputStream,
                status = e.status,
                contentType = HttpContentType.HTML,
                bodyLength = 0,
            )
        } catch (e: IOException) {
            log.error(e.message)
        } finally {
            input.close()
            output.close()
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RequestHandler::class.java)
    }
}
