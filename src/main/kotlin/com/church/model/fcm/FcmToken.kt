package com.church.model.fcm

import com.church.model.account.Account
import com.church.model.chat.ChatRoomType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne



@Entity
data class FcmToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val account: Account,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var deviceType: DeviceType,

    val createdAt: Long = System.currentTimeMillis(),

    var updatedAt: Long = System.currentTimeMillis()
)
