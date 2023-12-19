package util

import java.io.BufferedReader

object IOUtils {
    fun readData(
        br: BufferedReader,
        contentLength: Int,
    ): String {
        val body = CharArray(contentLength)
        br.read(body, 0, contentLength)
        return body.joinToString("")
    }
}
