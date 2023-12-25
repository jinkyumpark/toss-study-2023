package exception

import model.HttpStatus
import java.lang.IllegalStateException

class ResponseStatusException(
    val status: HttpStatus,
) : IllegalStateException()
