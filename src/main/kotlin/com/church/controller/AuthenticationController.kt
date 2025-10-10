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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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
        @AuthenticationPrincipal user: User,
        @RequestBody refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<RefreshTokenResponse> {
        return ResponseEntity.ok(authService.generateAccessTokenFromRefreshToken(user,refreshTokenRequest))
    }
}
