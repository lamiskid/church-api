package com.church.service

import com.church.model.account.Account
import com.church.model.profile.ProfileMedia
import com.church.payload.profile.media.ConfirmUploadRequest
import com.church.payload.profile.media.ProfileMediaResponse
import com.church.repository.ProfileMediaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileMediaService(
    private val profileMediaRepository: ProfileMediaRepository
) {

    @Transactional
    fun confirmUpload(account: Account, request: ConfirmUploadRequest): ProfileMediaResponse {
        /*val media = account.id?.let {
            profileMediaRepository
                .findAllByAccountId(it)
                .find { it.signedUploadUrl == request.signedUploadUrl }
        }
            ?: throw ApiException("Signed URL not found or expired")

        val updated = media.copy(
            mediaUrl = request.mediaUrl,
            approved = true
        )*/
        val profileMedia1 = ProfileMedia(
            account = account,
            signedUploadUrl = request.signedUploadUrl,
            mediaUrl = request.mediaUrl
        )
        profileMediaRepository.save(profileMedia1)

        return ProfileMediaResponse(
            type = profileMedia1.type,
            mediaUrl = profileMedia1.mediaUrl
        )
    }

    fun getMediaForAccount(account: Account): List<ProfileMediaResponse> =
        profileMediaRepository.findAllByAccountId(account.id!!)
            .map {
                ProfileMediaResponse(
                    type = it.type,
                    mediaUrl = it.mediaUrl
                )
            }
}
