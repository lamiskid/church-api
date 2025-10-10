package com.church.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class CentrifugoService(
      @Value("\${centrifugo.api-url}") private val apiUrl: String,
    @Value("\${centrifugo.api-key}") private val apiKey: String
) {
    private val restTemplate = RestTemplate()
    private val log = LoggerFactory.getLogger(CentrifugoService::class.java)

    fun publish(channel: String, data: Any) {
        try {
            val headers = HttpHeaders().apply {
                contentType = MediaType.APPLICATION_JSON
                set("Authorization", "apikey $apiKey")
            }

            val payload = mapOf(
                "method" to "publish",
                "params" to mapOf(
                    "channel" to channel,
                    "data" to data
                )
            )

            val request = HttpEntity(payload, headers)
            restTemplate.exchange<String>(apiUrl, org.springframework.http.HttpMethod.POST, request)

            log.info("Published event to Centrifugo channel=$channel")
        } catch (ex: Exception) {
            log.error("Failed to publish to Centrifugo: ${ex.message}")
        }
    }
}