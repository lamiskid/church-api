package com.church.model.sermons
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity

@Table(name = "sermons")
data class Sermon(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(nullable = false)
    var preacher: String,

    @OneToOne(mappedBy = "sermon", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var media: SermonMedia? = null,

    @Column(nullable = false)
    val createdBy: UUID,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime? = null
)


