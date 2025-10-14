package com.church.exception

import org.springframework.http.HttpStatus

class ApiException(
    override val message: String,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
    val errorCode: String? = null,
    val causeThrowable: Throwable? = null
) : RuntimeException(message, causeThrowable)
