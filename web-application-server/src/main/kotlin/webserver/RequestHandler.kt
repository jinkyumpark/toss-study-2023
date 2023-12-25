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
            val input = connection.getInputStream()
            val output = connection.getOutputStream()

            try {
                val dos = DataOutputStream(output)
                val body = "Hello World".toByteArray()
                response200Header(dos, body.size)
                responseBody(dos, body)
            } finally {
                input.close()
                output.close()
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
            dos.apply {
                writeBytes("HTTP/1.1 200 OK \r\n")
                writeBytes("Content-Type: text/html;charset=utf-8\r\n")
                writeBytes("Content-Length: $lengthOfBodyContent\r\n")
                writeBytes("\r\n")
            }
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
