package com.church.repository

import com.church.model.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface AccountRepository : JpaRepository<Account, UUID> {

   fun findByUsername(username: String): Account?
    fun existsByUsername(username: String): Boolean
    fun findByEmail(username: String): Account?
}