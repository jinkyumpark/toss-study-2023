package webserver

import model.HttpContentType
import model.HttpStatus
import org.slf4j.LoggerFactory
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

            val body = Files.readAllBytes(Path.of("./webapp$url"))
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
        } catch (e: IOException) {
            val dataOutputStream = DataOutputStream(output)
            HttpResponseUtils.applyHttpHeader(
                dos = dataOutputStream,
                status = HttpStatus.NOT_FOUND,
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
