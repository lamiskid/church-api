package com.church.service

import com.church.model.prayerrequest.PrayerRequest
import com.church.payload.pagination.PageResponse
import com.church.payload.pagination.PaginationMapper.toPageResponse
import com.church.payload.prayerrequest.CreatePrayerRequest
import com.church.payload.prayerrequest.PrayerRequestResponse
import com.church.repository.PrayerRequestRepository
import com.church.security.User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
 class PrayerRequestService (
    private val prayerRequestRepository: PrayerRequestRepository
){

    @Transactional
    open fun createPrayerRequest(user: User, createPrayerRequest: CreatePrayerRequest): PrayerRequestResponse {
        val prayer = PrayerRequest(
            title = createPrayerRequest.title,
            message = createPrayerRequest.message,
            account = user.toAccount(),
            approved = true,
            isAnonymous = createPrayerRequest.isAnonymous
        )
        val saved = prayerRequestRepository.save(prayer)
        return saved.toDto()
    }

    fun getApprovedPrayerRequests(page: Int, size: Int): PageResponse<PrayerRequestResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val pageData = prayerRequestRepository.findAllByApprovedTrue(pageable)
        return toPageResponse(pageData) { it.toDto() }
    }

    fun getPendingPrayerRequests(page: Int, size: Int): PageResponse<PrayerRequestResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val pageData = prayerRequestRepository.findAllByApprovedFalse(pageable)
        return toPageResponse(pageData) { it.toDto() }
    }

    @Transactional
    open fun approvePrayerRequest(id: UUID): PrayerRequestResponse? {
        val prayer = prayerRequestRepository.findById(id).get()
        val updated = prayer.copy(approved = true)
        val saved = prayerRequestRepository.save(updated)
        return saved.toDto()
    }

    private fun PrayerRequest.toDto(): PrayerRequestResponse = PrayerRequestResponse(
        id = id,
        title = title,
        message = message,
        submittedBy = "",
        isAnonymous = isAnonymous,
        createdAt = createdAt
    )
}