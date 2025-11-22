package com.church.controller


import com.church.payload.LoginRequest
import com.church.payload.LoginResponse
import com.church.payload.RegisterRequest
import com.church.payload.refreshtoken.RefreshTokenRequest
import com.church.payload.refreshtoken.RefreshTokenResponse
import com.church.security.User
import com.church.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authService: AuthenticationService
){

    @PostMapping("/register")
    fun createUser(@RequestBody registerRequest: RegisterRequest): ResponseEntity<String> {
        authService.register(registerRequest)
        return ResponseEntity.ok("Registration successful!")
    }

    @PostMapping("/login")
    fun authenticateAndGetToken(@RequestBody authRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authService.login(authRequest))
    }

    @PostMapping("/refresh")
    fun refreshAuthenticationToken(
        @RequestBody refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<RefreshTokenResponse> {
        return ResponseEntity.ok(authService.generateAccessTokenFromRefreshToken(refreshTokenRequest))
    }


    @PostMapping("/resend-verification-token")
    fun resendVerificationToken(@RequestParam email: String): ResponseEntity<String> {
        authService.resendVerificationToken(email)
        return ResponseEntity.ok("Verification token resent to $email")
    }


   /* @GetMapping("/verify")
    fun verifyEmail(@RequestParam token: String): ResponseEntity<String> {
        val verificationToken = verificationTokenRepo.findByToken(token)
            ?: return ResponseEntity.badRequest().body("Invalid token")

        if (verificationToken.expiryDate.isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired")
        }

        val user = verificationToken.user
        user.enabled = true
        userRepository.save(user)
        verificationTokenRepo.delete(verificationToken)

        return ResponseEntity.ok("Email verified successfully")
    }


    @PostMapping("/reset-password-request")
    fun requestPasswordReset(@RequestParam email: String): ResponseEntity<String> {
        val user = userRepository.findByEmail(email)
            ?: return ResponseEntity.badRequest().body("No user with that email")

        val token = UUID.randomUUID().toString()
        val resetToken = PasswordResetToken(
            token = token,
            user = user,
            expiryDate = LocalDateTime.now().plusHours(1)
        )
        passwordResetTokenRepo.save(resetToken)

        val resetUrl = "http://localhost:8080/api/reset-password?token=$token"
        emailService.sendEmail(email, "Reset Password", "Click: $resetUrl")

        return ResponseEntity.ok("Password reset email sent")
    }





    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestParam token: String,
        @RequestParam newPassword: String
    ): ResponseEntity<String> {
        val resetToken = passwordResetTokenRepo.findByToken(token)
            ?: return ResponseEntity.badRequest().body("Invalid token")

        if (resetToken.expiryDate.isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired")
        }

        val user = resetToken.user
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
        passwordResetTokenRepo.delete(resetToken)

        return ResponseEntity.ok("Password successfully reset")
    }*/




}
