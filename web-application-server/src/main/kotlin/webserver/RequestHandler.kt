package webserver

import exception.ResponseStatusException
import model.HttpContentType
import model.HttpMethod
import org.slf4j.LoggerFactory
import service.GetUserListRequestService
import service.PostUserCreateRequestService
import service.PostUserLoginRequestService
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
            val reader = input.bufferedReader()
            val headersRaw = StringBuilder()
            var line: String? = reader.readLine()
            while (!line.isNullOrBlank()) {
                headersRaw.append(line).append("\r\n")
                line = reader.readLine()
            }
            val headers = HttpRequestUtils.parseHeaders(headersRaw.toString())

            val contentLength = headers["Content-Length"]?.toInt() ?: 0
            val bodyRaw = CharArray(contentLength)
            if (contentLength > 0) {
                reader.read(bodyRaw, 0, contentLength)
            }

            val method = HttpRequestUtils.parseMethod(headersRaw.toString())
            val url = HttpRequestUtils.parseUrl(headersRaw.toString())
            val body = HttpRequestUtils.parseBody(bodyRaw.joinToString(""))
            val cookie = HttpRequestUtils.parseCookies(headers["Cookie"] ?: "")

            val responseBody = when {
                method == HttpMethod.GET && url == "/" -> {
                    ResourceRequestService.process(
                        method = method,
                        url = "/index.html",
                        headers = headers,
                        body = body,
                    )
                }

                method == HttpMethod.POST && url.startsWith("/user/create") -> {
                    PostUserCreateRequestService.process(
                        headers = headers,
                        body = body,
                    )
                }

                method == HttpMethod.POST && url.startsWith("/user/login") -> {
                    PostUserLoginRequestService.process(
                        headers = headers,
                        body = body,
                        cookie = cookie,
                    )
                }

                method == HttpMethod.GET && url.startsWith("/user/list") -> {
                    GetUserListRequestService.process(
                        headers = headers,
                        body = body,
                        cookie = cookie,

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
                status = responseBody.status,
                contentType = responseBody.body.contentType,
                bodyLength = responseBody.body.data.size,
                headers = responseBody.headers,
            )
            HttpResponseUtils.applyHttpBody(
                dos = dataOutputStream,
                body = responseBody.body.data,
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
