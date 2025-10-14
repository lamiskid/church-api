package com.church.exception

import org.springframework.http.HttpStatus
import java.time.Instant

data class ErrorResponse(
    var message: String?,
    var httpStatus: HttpStatus,
    val path: String,
    val timestamp: Instant = Instant.now()
)