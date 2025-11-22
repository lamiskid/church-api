package com.church.model.prayerrequest

import com.church.model.account.Account
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class PrayerRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val message: String,

    @ManyToOne
    val account: Account,

    val isAnonymous: Boolean = false,

    @Column(nullable = false)
    val createdAt: Long = 0,

    val approved: Boolean = false
)
