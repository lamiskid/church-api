package com.church.model.donation

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "donations")
class Donation(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val amount: Long,

    @Column(nullable = false)
    val currency: String,

    @Column(nullable = false, unique = true)
    val paymentIntentId: String,

    @Column(nullable = false)
    var status: String,

    val donorEmail: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)