package com.church.model.profile

import com.church.model.account.Account
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "profiles")
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    val firstName: String,

    @Column(nullable = false)
    val lastName: String,
    val phone: String? = null,
    val address: String? = null,
    val phoneNumber:String?=null,
    val profilePictureUrl: String? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true,)
    val account: Account
)
