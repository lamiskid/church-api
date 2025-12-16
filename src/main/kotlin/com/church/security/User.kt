package com.church.security

import com.church.model.account.Account
import com.church.model.account.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class User(
    private val userId: UUID,
    private val password: String,
    private val email: String,
    private val authorities: List<GrantedAuthority>
) : UserDetails {

    constructor(user: Account, roles: List<UserRole>) : this(
        userId = user.id!!,
        password = user.password,
        email = user.email,
        authorities = roles.map {
            SimpleGrantedAuthority("ROLE_${it.roleType.name}")
        }
    )

    fun toAccount(): Account {
        return Account(
            id = this.userId!!,
            email = this.email,
        )
    }

    fun getId(): UUID = userId

    override fun getUsername(): String = email

    override fun getPassword(): String = password

    override fun isEnabled(): Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true
}
