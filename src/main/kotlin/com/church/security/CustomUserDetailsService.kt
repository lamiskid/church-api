package com.church.security

import com.church.exception.ApiException
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
        val account = accountRepository.findByUsername(username)
            ?: throw ApiException("User not found")
        val accountRoles = roleRepository.findAccountRoles(account.id!!)
        return User(account, accountRoles)
    }
}
