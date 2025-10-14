package com.church.security

import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val token = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        try {
            if (token != null && SecurityContextHolder.getContext().authentication == null) {
                val username = jwtUtil.extractUsername(token)

                val userDetails = userDetailsService.loadUserByUsername(username)

                if (jwtUtil.isTokenValid(token, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }

            filterChain.doFilter(request, response)

        } catch (_: ExpiredJwtException) {
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired")
        } catch (ex: Exception) {
            respondWithError(response, HttpServletResponse.SC_FORBIDDEN, ex.localizedMessage)
        }
    }

    private fun respondWithError(response: HttpServletResponse, status: Int, message: String) {
        response.status = status
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"

        val errorJson = """{
            "timestamp": "${Instant.now()}",
            "status": $status,
            "error": "${HttpStatus.valueOf(status).reasonPhrase}",
            "message": "$message"
        }""".trimIndent()

        response.writer.write(errorJson)
    }
}
