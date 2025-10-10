package com.church.payload

data class LoginResponse(
    val username: String,
   val accessToken: String,
    val refreshToken: String,
    val roles: List<String>
)