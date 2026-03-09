package com.church.model.account

import com.church.model.profile.Profile
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import java.util.UUID

@Entity
class Account(

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val email:String,

    @Column(nullable = false)
    val password:String,

    @Enumerated(EnumType.STRING)
    val accountStatus: AccountStatus = AccountStatus.ACTIVE,

    @Column(name = "created_at", nullable = false)
    val createdAt: Long = System.currentTimeMillis(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: Long =System.currentTimeMillis(),

    @OneToOne(mappedBy = "account", cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY)
    val profile: Profile? = null,

    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL],
        orphanRemoval = true,fetch = FetchType.LAZY)
    val userRoles: MutableSet<UserRole> = mutableSetOf(),

){
    constructor(id: UUID,email: String) :
            this(id,email,"")
}
