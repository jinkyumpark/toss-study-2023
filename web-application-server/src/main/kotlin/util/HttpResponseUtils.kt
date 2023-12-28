package util

import http.*
import org.slf4j.LoggerFactory
import java.io.DataOutputStream

object HttpResponseUtils {

    private val log = LoggerFactory.getLogger(HttpResponseUtils::class.java)

    fun applyHttpResponse(
        dos: DataOutputStream,
        response: HttpResponse,
    ) {
        try {
            dos
                .apply {
                    writeBytes("HTTP/1.1 ${response.status.code} ${response.status.displayName}\r\n")
                    writeBytes("${HttpHeader.Key.CONTENT_TYPE.raw}: ${response.body.contentType.raw};charset=utf-8\r\n")
                    writeBytes("${HttpHeader.Key.CONTENT_LENGTH.raw}: ${response.body.data.size}\r\n")

                    response.headers
                        .filter {
                            it.key !in listOf(
                                HttpHeader.Key.CONTENT_TYPE.raw,
                                HttpHeader.Key.CONTENT_LENGTH.raw,
                            )
                        }
                        .forEach { (key, value) ->
                            writeBytes("${key}: $value\r\n")
                        }

                    writeBytes("\r\n")
                }

                dos.write(response.body.data, 0, response.body.data.size)
                dos.flush()
        } catch (e: Exception) {
            log.error(e.message)
        }
    }
}
