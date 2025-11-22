package com.church.repository

import com.church.model.fcm.FcmToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface FcmTokenRepository: JpaRepository<FcmToken, Long> {

    fun findByToken(token: String): Optional<FcmToken>
    fun findAllByAccountId(account_id: UUID): List<FcmToken>
}