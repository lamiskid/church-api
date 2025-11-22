package com.church.repository

import com.church.model.verification.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository : JpaRepository<VerificationToken, Long> {
    fun findByEmailAndUsedFalse(email: String): List<VerificationToken>

}
