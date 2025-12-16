package com.church.service


import com.church.exception.ApiException
import com.church.model.devotional.Devotional
import com.church.model.devotional.DevotionalBookmark
import com.church.payload.devotiona.DevotionalResponse
import com.church.payload.devotional.DevotionalRequest
import com.church.payload.pagination.PageResponse
import com.church.payload.pagination.PaginationMapper.toPageResponse
import com.church.repository.DevotionalBookmarkRepository
import com.church.repository.DevotionalRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class DevotionalService(
    private val devotionalRepository: DevotionalRepository,
    private val bookmarkRepository: DevotionalBookmarkRepository
) {

    fun create(request: DevotionalRequest): DevotionalResponse {
        val devotional = Devotional(
            title = request.title,
            content = request.content,
            scripture = request.scripture,
        )
        return devotionalRepository.save(devotional).toResponse()
    }

    fun getAll(page: Int, size: Int, userId: UUID?): PageResponse<DevotionalResponse> {
        val pageable = PageRequest.of(page, size)
        val pageData = devotionalRepository.findAll(pageable)

        val bookmarkedIds =
            if (userId != null) {
                bookmarkRepository.findBookmarkedIds(
                    userId,
                    pageData.content.map { it.id }
                )
            } else emptyList()

        return toPageResponse(pageData) { devotional ->
            devotional.toResponse(
                bookmarked = devotional.id in bookmarkedIds
            )
        }
    }




    /*  fun getAll(page:Int,size:Int, userId: UUID): PageResponse<DevotionalResponse> {
          val pageable: Pageable = PageRequest.of(page, size)
          val pageData = devotionalRepository.findAll(pageable)
         return toPageResponse(pageData){
              val bookmarked = if (userId != null)
                  bookmarkRepository.existsByUserIdAndDevotional_Id(userId, it.id)
              else false
              it.toResponse(bookmarked)
          }
      }*/

    fun getOne(id: Long, userId: UUID): DevotionalResponse {
        val devo = devotionalRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Devotional not found") }

        val bookmarked = if (userId != null)
            bookmarkRepository.existsByUserIdAndDevotional_Id(userId, id)
        else false

        return devo.toResponse(bookmarked)
    }

    fun bookmark(userId: UUID, devotionalId: Long) {
        if (bookmarkRepository.existsByUserIdAndDevotional_Id(userId, devotionalId)) return

        val devotional = devotionalRepository.findById(devotionalId)
            .orElseThrow { IllegalArgumentException("Devotional not found") }

        bookmarkRepository.save(
            DevotionalBookmark(userId = userId, devotional = devotional)
        )
    }

    fun toggleBookmark(userId: UUID, devotionalId: Long): Boolean {
        val existing = bookmarkRepository.findByUserIdAndDevotionalId(userId, devotionalId)

        return if (existing != null) {
            bookmarkRepository.delete(existing)
            false
        } else {
            val devotional = devotionalRepository.findById(devotionalId)
                .orElseThrow { ApiException("Devotional not found: $devotionalId") }

            bookmarkRepository.save(
                DevotionalBookmark(userId = userId, devotional = devotional)
            )
            true
        }
    }


    fun Devotional.toResponse(bookmarked: Boolean = false) =
        DevotionalResponse(
            id, title, content, scripture, Instant.now(), bookmarked
        )

}
