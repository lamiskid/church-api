package com.church.security

import com.church.repository.AccountRepository
import com.church.repository.UserRoleRepository

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val accountRepository: AccountRepository,
    private val roleRepository: UserRoleRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = accountRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")

        if (user.id == null) throw UsernameNotFoundException("User not found")

        val accountRoles = roleRepository.findAccountRoles(user.id!!)
        return User(user, accountRoles)
    }
}
