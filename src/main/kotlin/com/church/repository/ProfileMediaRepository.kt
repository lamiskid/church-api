package com.church.repository

import com.church.model.profile.ProfileMedia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProfileMediaRepository : JpaRepository<ProfileMedia, Long> {
    fun findAllByAccountId(accountId: UUID): List<ProfileMedia>
}