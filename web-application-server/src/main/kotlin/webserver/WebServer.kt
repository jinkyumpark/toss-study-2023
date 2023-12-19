package webserver

import org.slf4j.LoggerFactory
import java.io.IOError
import java.net.ServerSocket

class WebServer {
    fun main(args: Array<String>?) {
        val port = when {
            args.isNullOrEmpty() -> DEFAULT_PORT
            else -> args.first().toInt()
        }

        try {
            ServerSocket(port).use { listenSocket ->
                log.info("Web Application Server started at port $port")
                while (true) {
                    val connection = listenSocket.accept() ?: break
                    val requestHandler = RequestHandler(connection)
                    requestHandler.start()
                }
            }
        } catch (e: IOError) {
            log.error(e.message)
        }
    }

    companion object {
        private const val DEFAULT_PORT = 8080
        private val log = LoggerFactory.getLogger(Companion::class.java)
    }
}