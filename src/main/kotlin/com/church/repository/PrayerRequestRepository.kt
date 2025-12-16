package com.church.repository

import com.church.model.prayer.PrayerRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


@Repository
interface PrayerRequestRepository : JpaRepository<PrayerRequest, Long> {

    fun findAllByApprovedTrue(pageable: Pageable): Page<PrayerRequest>

    fun findAllByApprovedFalse(pageable: Pageable): Page<PrayerRequest>
}