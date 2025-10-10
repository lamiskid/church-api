package com.church.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleResourceNotFoundException(exception: ResourceNotFoundException): ErrorResponse {
        return ErrorResponse(
            message = exception.message,
            httpStatus = HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleUsernameNotFoundException(exception: UsernameNotFoundException): ErrorResponse {
        return ErrorResponse(
            message = exception.message,

            httpStatus = HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNotFound(ex: NoHandlerFoundException): ResponseEntity<Map<String, Any>> {
        val body = mapOf(
            "error" to "Not Found",
            "message" to "Path '${ex.requestURL}' not found",
            "status" to HttpStatus.NOT_FOUND.value()
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }

}