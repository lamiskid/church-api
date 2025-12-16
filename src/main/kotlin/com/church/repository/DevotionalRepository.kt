package com.church.repository

import com.church.model.devotional.Devotional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface DevotionalRepository : JpaRepository<Devotional, Long> {

    fun findByDate(date: LocalDate): Optional<Devotional>

}
