package com.church.model.chat

import com.church.model.account.Account
import jakarta.persistence.*

@Entity
@Table(name = "chat_rooms")
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: ChatRoomType = ChatRoomType.GROUP,

    @Column(name = "channel_id", nullable = false, unique = true)
    val channelId: String,

    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    val messages: MutableList<Message> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = System.currentTimeMillis(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long = System.currentTimeMillis(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat_room_users",
        joinColumns = [JoinColumn(name = "chat_room_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val participants: MutableSet<Account> = mutableSetOf()

)

