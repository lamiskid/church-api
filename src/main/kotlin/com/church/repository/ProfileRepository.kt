package com.church.repository

import com.church.model.profile.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : JpaRepository<Profile, UUID> {
    fun findByAccountId(accountId: UUID): Profile?
    fun existsByAccountId(accountId: UUID): Boolean

}