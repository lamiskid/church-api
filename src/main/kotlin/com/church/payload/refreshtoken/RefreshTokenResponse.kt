package com.church.payload.refreshtoken

data class RefreshTokenResponse (
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int? = null,
)