package com.church.security


import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil {

    private val SECRET_KEY = "your_256_bit_secret_key_that_is_long_enough_for_hmac_sha" // base64-encoded or plain

    private fun getSignKey(): Key {
        val keyBytes = SECRET_KEY.toByteArray() // or use: Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    fun generateToken(user: User): String {
        val roles = user.authorities
            .map { it.authority.replaceFirst("^ROLE_".toRegex(), "") }

        val claims = mapOf(
            "userId" to user.getId(),
            "roles" to roles
        )

        return createToken(claims, user.username)
    }

    private fun createToken(claims: Map<String, Any>, userName: String): String {
        val extendedClaims = claims.toMutableMap()
        extendedClaims["channels"] = listOf("channel")

        return Jwts.builder()
            .setClaims(extendedClaims)
            .setSubject(userName)
            .setIssuedAt(Date(System.currentTimeMillis()))
            //.setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 5))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }


   /* fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact()
    }*/

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }
}


