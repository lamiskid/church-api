package com.church.model.refreshtoken

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID
import jakarta.persistence.*

@Entity
@Table(name = "refresh_tokens")
data class RefreshToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(name = "expires_at")
    val expiresAt: Long? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long,

    @Column(name = "user_id", nullable = false)
    val userId: UUID
)


