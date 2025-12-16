package com.church.model.profile

import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY


@Entity
data class ProfileMedia(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val type:MediaType = MediaType.IMAGE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    val profile: Profile,

    @Column(nullable = false, columnDefinition = "TEXT")
    val signedUploadUrl:String,

    @Column(columnDefinition = "TEXT")
    val mediaUrl:String,

    @Column(nullable = false)
    val createdAt: Long = System.currentTimeMillis(),

    val approved: Boolean = false
)