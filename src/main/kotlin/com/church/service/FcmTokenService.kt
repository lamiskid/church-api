package com.church.service

import com.church.model.fcm.FcmToken
import com.church.payload.fcm.FcmTokenMapper
import com.church.payload.fcm.FcmTokenRequest
import com.church.payload.fcm.FcmTokenResponse
import com.church.repository.AccountRepository
import com.church.repository.FcmTokenRepository
import com.church.security.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class FcmTokenService(
    private val fcmTokenRepository: FcmTokenRepository,
    private val accountRepository: AccountRepository
) {

    @Transactional
    fun registerOrUpdateToken(user: User, request: FcmTokenRequest): FcmTokenResponse {
        val account = user.toAccount()

        val existingToken = fcmTokenRepository.findByToken(request.token)


        val updatedToken = if (existingToken.isPresent) {
            val token = existingToken.get()
            token.deviceType = request.deviceType
            token.updatedAt = System.currentTimeMillis()
            token
        } else {
            FcmToken(
                token = request.token,
                account = account,
                deviceType = request.deviceType
            )
        }

        val saved = fcmTokenRepository.save(updatedToken)
        return FcmTokenMapper.toResponse(saved)
    }

    fun getTokensByAccountId(accountId: UUID): List<FcmTokenResponse> {
        return fcmTokenRepository.findAllByAccountId(accountId)
            .map { FcmTokenMapper.toResponse(it) }
    }

    @Transactional
    fun deleteToken(token: String) {
        fcmTokenRepository.findByToken(token)
            .ifPresent { fcmTokenRepository.delete(it) }
    }
}