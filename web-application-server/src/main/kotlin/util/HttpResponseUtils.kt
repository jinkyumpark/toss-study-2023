package util

import model.HttpContentType
import model.HttpHeader
import model.HttpStatus
import org.slf4j.LoggerFactory
import java.io.DataOutputStream

object HttpResponseUtils {

    private val log = LoggerFactory.getLogger(HttpResponseUtils::class.java)

    fun applyHttpHeader(
        dos: DataOutputStream,
        status: HttpStatus,
        contentType: HttpContentType,
        bodyLength: Int,
        headers: Map<HttpHeader, String> = mapOf(),
    ) {
        try {
            dos.apply {
                writeBytes("HTTP/1.1 ${status.code} ${status.displayName}\r\n")
                writeBytes("${HttpHeader.CONTENT_TYPE}: ${contentType.raw};charset=utf-8\r\n")
                writeBytes("${HttpHeader.CONTENT_LENGTH.key}: $bodyLength\r\n")

                headers
                    .filter { it.key !in listOf(HttpHeader.CONTENT_TYPE, HttpHeader.CONTENT_LENGTH) }
                    .forEach { (key, value) ->
                        writeBytes("${key.key}: $value\r\n")
                    }

                writeBytes("\r\n")
            }
        } catch (e: Exception) {
            log.error(e.message)
        }
    }

    fun applyHttpBody(
        dos: DataOutputStream,
        body: ByteArray,
    ) {
        try {
            dos.write(body, 0, body.size)
            dos.flush()
        } catch (e: Exception) {
            log.error(e.message)
        }
    }
}
