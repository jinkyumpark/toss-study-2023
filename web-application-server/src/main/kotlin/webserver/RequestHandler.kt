package webserver

import org.slf4j.LoggerFactory
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket

class RequestHandler(
    private val connection: Socket,
) : Thread() {

    override fun run() {
        log.debug("New Client Connect! Connected IP : ${connection.getInetAddress()}, Port : ${connection.getPort()}")
        try {
            connection.getInputStream().use {
                connection.getOutputStream().use { outputStream ->
                    val dos = DataOutputStream(outputStream)
                    val body = "Hello World".toByteArray()
                    response200Header(dos, body.size)
                    responseBody(dos, body)
                }
            }
        } catch (e: IOException) {
            log.error(e.message)
        }
    }

    private fun response200Header(
        dos: DataOutputStream,
        lengthOfBodyContent: Int,
    ) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n")
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n")
            dos.writeBytes("Content-Length: $lengthOfBodyContent\r\n")
            dos.writeBytes("\r\n")
        } catch (e: IOException) {
            log.error(e.message)
        }
    }

    private fun responseBody(
        dos: DataOutputStream,
        body: ByteArray,
    ) {
        try {
            dos.write(body, 0, body.size)
            dos.flush()
        } catch (e: IOException) {
            log.error(e.message)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(RequestHandler::class.java)
    }
}
