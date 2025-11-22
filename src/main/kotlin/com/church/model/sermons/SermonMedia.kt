package com.church.model.sermons

import com.church.model.account.Account
import com.church.model.profile.MediaType
import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY

@Entity
data class SermonMedia (
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    @OneToOne
    val sermon: Sermon,

    @Enumerated(EnumType.STRING)
    val type: MediaType = MediaType.IMAGE,

    @ManyToOne
    val account: Account,

    @Column(nullable = false, columnDefinition = "TEXT")
    val signedUploadUrl:String,

    val mediaUrl:String,

    @Column(nullable = false)
    val createdAt: Long = System.currentTimeMillis(),

    val approved: Boolean = false
)