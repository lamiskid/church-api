package com.church.payload

data class RegisterRequest(
    val password: String,
    val email: String,
)
