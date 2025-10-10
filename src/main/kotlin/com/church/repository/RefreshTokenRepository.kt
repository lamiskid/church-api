package com.church.repository

import com.church.model.refreshtoken.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface RefreshTokenRepository :JpaRepository<RefreshToken, Long>{
     fun findByToken(token: String):RefreshToken?
}

