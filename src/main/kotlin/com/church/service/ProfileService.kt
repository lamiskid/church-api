package com.church.service

import com.church.exception.ApiException
import com.church.model.profile.Profile
import com.church.payload.profile.ProfileRequest
import com.church.payload.profile.ProfileResponse
import com.church.payload.profile.toDto
import com.church.repository.AccountRepository
import com.church.repository.ProfileRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val accountRepository: AccountRepository
) {

    fun getProfile(accountId: UUID): ProfileResponse {
        val profile = profileRepository.findByAccountId(accountId)
            ?: throw ApiException("Profile not found for account $accountId")

        return profile.toDto()
    }

    fun createProfile(accountId: UUID, request: ProfileRequest): ProfileResponse {
        val account = accountRepository.findById(accountId)
            .orElseThrow { ApiException("Account not found: $accountId") }

        val profile = Profile(
            firstName = request.firstName,
            lastName = request.lastName,
            phone = request.phone,
            address = request.address,
            profilePictureUrl = request.profilePictureUrl,
            account = account
        )

        return profileRepository.save(profile).toDto()
    }




    fun updateProfile(accountId: UUID, request: ProfileRequest): ProfileResponse {

        val existing = profileRepository.findByAccountId(accountId)

        return if (existing != null) {
            val updated = Profile(
                id = existing.id,
                firstName = request.firstName,
                lastName = request.lastName,
                phone = request.phone,
                address = request.address,
                profilePictureUrl = request.profilePictureUrl,
                account = existing.account
            )

            profileRepository.save(updated).toDto()

        } else {

            val account = accountRepository.findById(accountId)
                .orElseThrow { ApiException("Account not found: $accountId") }

            val newProfile = Profile(
                firstName = request.firstName,
                lastName = request.lastName,
                phone = request.phone,
                address = request.address,
                profilePictureUrl = request.profilePictureUrl,
                account = account
            )

            profileRepository.save(newProfile).toDto()
        }
    }

    fun updateProfileV3(accountId: UUID, request: ProfileRequest): ProfileResponse {
        val existing = profileRepository.findByAccountId(accountId)

        val account = existing?.account ?: accountRepository.findById(accountId)
            .orElseThrow { ApiException("Account not found: $accountId") }

        val profile = Profile(
            id = existing?.id,
            firstName = request.firstName,
            lastName = request.lastName,
            phone = request.phone,
            address = request.address,
            profilePictureUrl = request.profilePictureUrl,
            account = account
        )

        return profileRepository.save(profile).toDto()
    }



}
