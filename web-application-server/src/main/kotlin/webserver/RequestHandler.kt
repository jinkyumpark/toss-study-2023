package webserver

import exception.ResponseStatusException
import http.HttpHeader
import org.slf4j.LoggerFactory
import http.HttpResponse
import util.HttpRequestUtils
import util.HttpResponseUtils
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.Socket

class RequestHandler(
    private val connection: Socket,
) : Thread() {

    override fun run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort())

        val input = connection.getInputStream()
        val output = connection.getOutputStream()

        try {
            val request = HttpRequestUtils.getRequest(input)
            val controller = RequestMapping().getController(RequestMapping.Key(method = request.method, url = request.url.base))
            val response = controller.process(request)

            val dataOutputStream = DataOutputStream(output)
            HttpResponseUtils.applyHttpResponse(dos = dataOutputStream, response = response)
        } catch (e: ResponseStatusException) {
            val dataOutputStream = DataOutputStream(output)
            val errorResponse = HttpResponse(
                headers = listOf(),
                status = e.status,
                body = HttpResponse.Body(
                    contentType = HttpHeader.Value.ContentType.HTML,
                    data = byteArrayOf(),
                )
            )

            HttpResponseUtils.applyHttpResponse(dos = dataOutputStream, response = errorResponse)
        } catch (e: Exception) {
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
