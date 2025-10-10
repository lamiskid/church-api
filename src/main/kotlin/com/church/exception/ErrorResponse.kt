package com.church.exception

import org.springframework.http.HttpStatus

data class ErrorResponse(
    var message: String?,
    var httpStatus: HttpStatus
)