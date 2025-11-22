package com.church.model.profile

import com.church.model.account.Account
import jakarta.persistence.*
import jakarta.persistence.GenerationType.*


@Entity
data class ProfileMedia(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    @Enumerated(EnumType.STRING)
    val type:MediaType = MediaType.IMAGE,

    @ManyToOne
    val account: Account,

    @Column(nullable = false, columnDefinition = "TEXT")
    val signedUploadUrl:String,

    val mediaUrl:String,

    @Column(nullable = false)
    val createdAt: Long = System.currentTimeMillis(),

    val approved: Boolean = false
)