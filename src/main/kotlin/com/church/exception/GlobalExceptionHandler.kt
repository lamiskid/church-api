package com.church.exception

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class GlobalExceptionHandler {



    @ExceptionHandler(ApiException::class)
    fun handleApiException(
        ex: ApiException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            httpStatus = ex.status,
            message = ex.message,
            path = request.requestURI,
           // errorCode = ex.errorCode
        )
        return ResponseEntity.status(ex.status).body(response)
    }

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(
        ex: ApiException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            // status = ex.status.value(),
            httpStatus = ex.status,
            message = ex.message,
            path = request.requestURI,
            // errorCode = ex.errorCode
        )
        return ResponseEntity.status(ex.status).body(response)
    }


    @ExceptionHandler(ResourceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleResourceNotFoundException(
        exception: ResourceNotFoundException,
        request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(
            message = exception.message,
            httpStatus = HttpStatus.NOT_FOUND,
            path = request.requestURI,
        )
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleUsernameNotFoundException(
        exception: UsernameNotFoundException,
        request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(
            message = exception.message,
            httpStatus = HttpStatus.NOT_FOUND,
            path = request.requestURI,
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    fun handleBadCredentialsException(
        exception: UsernameNotFoundException,
        request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(
            message = exception.message,
            httpStatus = HttpStatus.NOT_FOUND,
            path = request.requestURI,
        )
    }

    @ExceptionHandler(ExpiredJwtException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleJwtException(
        exception: ExpiredJwtException,
        request: HttpServletRequest): ErrorResponse {
        return ErrorResponse(
            message = exception.message,
            httpStatus = HttpStatus.UNAUTHORIZED,
            path = request.requestURI,
        )
    }


    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleAuthenticationException(
        ex: AuthenticationException,
        request: HttpServletRequest
    ): ErrorResponse {
        return ErrorResponse(
            httpStatus = HttpStatus.UNAUTHORIZED,
            message = "Authentication is required",
            path = request.requestURI,

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