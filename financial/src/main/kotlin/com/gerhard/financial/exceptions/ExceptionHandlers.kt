package com.gerhard.financial.exceptions

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ExceptionHandlers {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ApiError<List<String>>> {
        val errors =
            ex.bindingResult.allErrors.map { "'${(it as FieldError).field}': ${it.defaultMessage}" }
        log.error("""Erro ao processar requisição: {}""", ex.message)

        val error = ResponseEntity(
            ApiError(
                code = HttpStatus.BAD_REQUEST.value(),
                status = HttpStatus.BAD_REQUEST.reasonPhrase,
                errors = errors,
                timestamp = LocalDateTime.now()
            ), HttpStatus.BAD_REQUEST
        )

        log.warn("Reportando os seguintes erros: {}", error)
        return error
    }

    @ExceptionHandler(BadRequestException::class)
    fun handlerBadRequestException(ex: BadRequestException): ResponseEntity<ApiError<String?>> {
        log.error("""Erro ao processar requisição: {}""", ex.message)

        val error = ResponseEntity(
            ApiError(
                code = HttpStatus.BAD_REQUEST.value(),
                status = HttpStatus.BAD_REQUEST.reasonPhrase,
                errors = ex.message,
                timestamp = LocalDateTime.now()
            ), HttpStatus.BAD_REQUEST
        )

        log.warn("Reportando os seguintes erros: {}", error)
        return error
    }

    @ExceptionHandler(ApplicationException::class)
    fun handlerCategoryHasRegisteredException(ex: ApplicationException): ResponseEntity<ApiError<List<String?>>> {
        log.error("""Erro ao processar requisição: {}""", ex.message)

        val error = ResponseEntity(
            ApiError(
                code = HttpStatus.UNPROCESSABLE_ENTITY.value(),
                status = HttpStatus.UNPROCESSABLE_ENTITY.reasonPhrase,
                errors = listOf(ex.message),
                timestamp = LocalDateTime.now()
            ), HttpStatus.UNPROCESSABLE_ENTITY
        )
        log.warn("Reportando os seguintes erros: {}", error)
        return error
    }

    companion object {
        val log = LoggerFactory.getLogger(ExceptionHandlers::class.java)
    }
}