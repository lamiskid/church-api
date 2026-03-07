package com.church.controller
import com.church.service.DonationService
import com.stripe.model.PaymentIntent
import com.stripe.net.Webhook
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/api/stripe")
class StripeWebhookController(
    private val donationService: DonationService,
   // @Value("\${stripe.webhook-secret}")

) {
    private val webhookSecret: String ="eeeee"
    @PostMapping("/webhook")
    fun handleWebhook(
        request: HttpServletRequest
    ): ResponseEntity<String> {

        val payload = request.reader.readText()
        val sigHeader = request.getHeader("Stripe-Signature")

        val event = try {
            Webhook.constructEvent(payload, sigHeader, webhookSecret)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature")
        }

        when (event.type) {

            "payment_intent.succeeded" -> {
                val paymentIntent =
                    event.dataObjectDeserializer.deserializeUnsafe() as PaymentIntent

                donationService.updateDonationStatus(paymentIntent)
            }

            "payment_intent.payment_failed" -> {
                val paymentIntent =
                    event.dataObjectDeserializer.deserializeUnsafe() as PaymentIntent

                donationService.updateDonationStatus(paymentIntent)
            }
        }

        return ResponseEntity.ok("Received")
    }
}