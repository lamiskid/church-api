package com.church.model.account

import jakarta.persistence.*
import java.util.UUID

@Entity
class Account(

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val email:String,
    val username:String,
    val password:String,
    val firstName:String,
    val lastName:String,
    val profilePicture: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = System.currentTimeMillis(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long =System.currentTimeMillis(),

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL],
        orphanRemoval = true,fetch = FetchType.LAZY)
    val userRoles: MutableSet<UserRole> = mutableSetOf()
){
    constructor(id: UUID,email: String,username: String) :
            this(id,email,username,"","","")

}