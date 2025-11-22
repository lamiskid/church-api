package com.church.model.verification

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "verification_tokens")
data class VerificationToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val expiresAt: LocalDateTime,

    @Column(nullable = false)
    val used: Boolean = false
)
