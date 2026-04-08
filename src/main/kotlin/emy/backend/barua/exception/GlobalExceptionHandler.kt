package emy.backend.barua.exception

import com.google.api.gax.rpc.*
import emy.backend.barua.utils.*
import org.slf4j.*
import org.springframework.context.annotation.*
import org.springframework.http.*
import org.springframework.web.bind.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.*
import java.time.*

@ControllerAdvice
@Profile(Mode.DEV)
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @ExceptionHandler(Exception::class)
    fun handleGenericException(e : Exception): ResponseEntity<ErrorResponseDto>{
        logger.error("Handle exception", e)
        val errorDto = ErrorResponseDto(
            "Internal server error",
            e.message.toString(),
            LocalDateTime.now()
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorDto)
    }

    @ExceptionHandler(value = [ResponseStatusException::class])
    fun handleStatusException(e:ResponseStatusException): ResponseEntity<MutableMap<String, Any>>{
        val map = mutableMapOf<String, Any>()
        map["message"] = e.reason.toString()
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(map)
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun handleEntityNotFound(e: NotFoundException): ResponseEntity<ErrorResponseDto>{
        logger.error("Handle entityNotFoundException", e);
        val errorDto = ErrorResponseDto(
            "Entity not found",
            e.message.toString(),
            LocalDateTime.now()
        )

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorDto)
    }

    @ExceptionHandler(value = [
        IllegalArgumentException::class,
        IllegalStateException::class,
        MethodArgumentNotValidException::class
    ])
    fun handleBadRequest(e : MethodArgumentNotValidException) : ResponseEntity<MutableMap<String, Any>> {
        logger.error("Handle handleBadRequest", e)
        val map = mutableMapOf<String, Any>()
        e.bindingResult.fieldErrors.forEach { error->
            map[error.field] = error.defaultMessage ?: "Validation"
        }
        val mapList = mutableMapOf<String, Any>()
        map.forEach { (p0, p1) ->
            mapList["message"] = p1
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(mapList)
    }

}