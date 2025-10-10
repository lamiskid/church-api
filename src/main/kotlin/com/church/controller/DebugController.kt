package com.church.controller

import com.church.payload.LoginRequest
import com.church.payload.LoginResponse
import com.church.payload.RegisterRequest
import com.church.service.AuthenticationService
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/cent")
class DebugController(
    private val authService: AuthenticationService
){

    private val CENTRIFUGO_API_KEY = "my_api_key"
    private val CENTRIFUGO_URL = "http://localhost:8000/api"

    @PostMapping("/publish")
    fun publish(
        @RequestParam channel: String,
        @RequestParam data: String
    ): ResponseEntity<String> {
        val restTemplate = RestTemplate()

        val body = mapOf(
            "method" to "publish",
            "params" to mapOf(
                "channel" to channel,
                "data" to mapOf("text" to data)
            )
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "apikey $CENTRIFUGO_API_KEY")
        }

        val entity = HttpEntity(body, headers)

        val response = restTemplate.postForEntity(CENTRIFUGO_URL, entity, String::class.java)
        println(entity.body)
        return ResponseEntity.ok(response.body)
    }
}