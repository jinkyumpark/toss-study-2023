package webserver

import exception.ResponseStatusException
import model.HttpContentType
import model.HttpMethod
import model.HttpStatus
import org.slf4j.LoggerFactory
import service.PostUserRequestService
import service.ResourceRequestService
import util.HttpRequestUtils
import util.HttpResponseUtils
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

class RequestHandler(
    private val connection: Socket,
) : Thread() {

    override fun run() {
        log.debug("New Client Connect! Connected IP : ${connection.getInetAddress()}, Port : ${connection.getPort()}")

        val input = connection.getInputStream()
        val output = connection.getOutputStream()

        try {
            val lines = input.bufferedReader().readLines()
            val headersRaw = lines.dropLast(2).joinToString("\r\n")
            val bodyRaw = lines.drop(1).last()

            val method = HttpRequestUtils.parseMethod(headersRaw)
            val url = HttpRequestUtils.parseUrl(headersRaw)
            val headers = HttpRequestUtils.parseHeaders(headersRaw)
            val body = HttpRequestUtils.parseBody(bodyRaw)

            val responseBody = when {
                method == HttpMethod.POST && url.startsWith("/user/create") -> {
                    PostUserRequestService.process(
                        headers = headers,
                        body = body,
                    )
                }

                method == HttpMethod.GET && url == "/" -> {
                    ResourceRequestService.process(
                        method = method,
                        url = "/index.html",
                        headers = headers,
                        body = body,
                    )
                }

                else -> {
                    ResourceRequestService.process(
                        method = method,
                        url = url,
                        headers = headers,
                        body = body,
                    )
                }
            }

            val dataOutputStream = DataOutputStream(output)
            HttpResponseUtils.applyHttpHeader(
                dos = dataOutputStream,
                status = HttpStatus.OK,
                contentType = HttpContentType.HTML,
                bodyLength = responseBody.size,
            )
            HttpResponseUtils.applyHttpBody(
                dos = dataOutputStream,
                body = responseBody,
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
