package com.church.service

import com.church.exception.ApiException
import com.church.model.sermons.Sermon
import com.church.model.sermons.SermonMedia
import com.church.payload.pagination.PageResponse
import com.church.payload.pagination.PaginationMapper
import com.church.payload.profile.media.ConfirmUploadRequest
import com.church.payload.s3.PresignedUploadResponse
import com.church.payload.sermon.SermonRequest
import com.church.payload.sermon.SermonResponse
import com.church.payload.sermon.toResponseDto
import com.church.repository.SermonMediaRepository
import com.church.repository.SermonRepository
import com.church.security.User
import com.church.util.S3ServiceUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class SermonService(
    private val sermonRepository: SermonRepository,
    private val sermonMediaRepository: SermonMediaRepository,
    private val s3ServiceUtil: S3ServiceUtil
) {

    @Transactional
    fun createSermon(sermonRequest: SermonRequest, authUserId: UUID): PresignedUploadResponse {
        val sermon = Sermon(
            title = sermonRequest.title,
            content = sermonRequest.content,
            preacher = sermonRequest.preacher,
            createdBy = authUserId
        )
        sermonRepository.save(sermon)
       return s3ServiceUtil.generatePresignedUploadUrl("",authUserId,"","mp3")
       // return sermonRepository.save(sermon).toResponseDto()
    }

    fun confirmSermonUpload(user: User, sermonId: UUID, confirmUploadRequest: ConfirmUploadRequest):SermonResponse{
        val sermonOptional = sermonRepository.findById(sermonId)
        if (sermonOptional.isPresent){
            val sermon = sermonOptional.get()
            val sermonMedia = SermonMedia(
                account = user.toAccount(),
                signedUploadUrl = confirmUploadRequest.signedUploadUrl,
                mediaUrl = confirmUploadRequest.mediaUrl,
                sermon = sermon)
            sermonMediaRepository.save(sermonMedia)
            return sermon.toResponseDto()
        }else{
            throw ApiException("Invalid sermon")
        }
    }

    @Transactional
    fun updateSermon( authUserId: UUID,id: UUID, sermonRequest: SermonRequest): SermonResponse{
        val sermon = sermonRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Sermon not found") }

        if (sermon.createdBy != authUserId) {
            throw ApiException("You are not allowed to edit this sermon")
        }

        sermon.title = sermonRequest.title
        sermon.content = sermonRequest.content
        sermon.preacher = sermonRequest.preacher
        sermon.updatedAt = LocalDateTime.now()

        return sermonRepository.save(sermon).toResponseDto()
    }


    fun getAllSermons(page: Int, size: Int): PageResponse<SermonResponse> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val sermons = sermonRepository.findAll(pageable)
        return PaginationMapper.toPageResponse(sermons) { it.toResponseDto() }
    }


    fun getSermonById(id: UUID): SermonResponse {
        val sermon = sermonRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Sermon not found") }
        return sermon.toResponseDto()
    }

    @Transactional
    fun deleteSermon(id: UUID, authUserId: UUID) {
        val sermon = sermonRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Sermon not found") }

        if (sermon.createdBy != authUserId) {
            throw IllegalAccessException("You are not allowed to delete this sermon")
        }

        sermonRepository.delete(sermon)
    }
}
