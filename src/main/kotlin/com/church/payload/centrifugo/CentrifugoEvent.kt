package com.church.payload.centrifugo

data class CentrifugoEvent(
    val type: String,
    val payload: Any
)