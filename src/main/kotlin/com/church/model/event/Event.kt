package com.church.model.event

import com.church.model.account.Account
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "events")
data class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    val id: Long = 0,

    val title: String,

    val description: String,

    @Column(name = "event_date")
    val eventDate: LocalDate,

    @Column(name = "start_time")
    val startTime: LocalTime,

    @Column(name = "end_time")
    val endTime: LocalTime,

    val location: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    val createdBy: Account,

    @OneToMany(mappedBy = "event", cascade = [CascadeType.ALL], orphanRemoval = true)
    val attendances: MutableList<EventAttendance> = mutableListOf()
)
