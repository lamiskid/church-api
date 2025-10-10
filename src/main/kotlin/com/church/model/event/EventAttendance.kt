package com.church.model.event



import com.church.model.account.Account
import jakarta.persistence.*

@Entity
@Table(name = "event_attendance")
data class EventAttendance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: Account,

    @Enumerated(EnumType.STRING)
    val status: AttendanceStatus
)
