package com.church.model.devotional

import jakarta.persistence.*
import java.util.UUID


@Entity
@Table(name = "devotional_bookmark")
data class DevotionalBookmark (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devotional_id", nullable = false)
    val devotional: Devotional
)
