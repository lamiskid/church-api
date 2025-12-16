package com.church.model.sermons

import com.church.model.account.Account
import com.church.model.profile.MediaType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sermon_media")
data class SermonMedia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sermon_id", nullable = false)
    val sermon: Sermon,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: MediaType = MediaType.AUDIO,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,

    @Column(nullable = false, columnDefinition = "TEXT")
    val signedUploadUrl: String,

    @Column(nullable = true)
    val mediaUrl: String? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val approved: Boolean = false
)