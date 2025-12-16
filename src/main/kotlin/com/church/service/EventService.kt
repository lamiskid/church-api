package com.church.service


import com.church.model.event.AttendanceStatus
import com.church.model.event.Event
import com.church.model.event.EventAttendance
import com.church.payload.event.EventAttendanceRequest
import com.church.payload.event.EventAttendanceResponse
import com.church.payload.event.EventRequest
import com.church.payload.event.EventResponse
import com.church.payload.event.toResponse
import com.church.payload.pagination.PageResponse
import com.church.payload.pagination.PaginationMapper.toPageResponse
import com.church.repository.AccountRepository
import com.church.repository.EventAttendanceRepository
import com.church.repository.EventRepository
import com.church.security.User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val eventAttendanceRepository: EventAttendanceRepository,
    private val userRepository: AccountRepository
) {

    @Transactional
    fun createEvent(user: User, request: EventRequest): EventResponse {

        val event = Event(
            title = request.title,
            description = request.description,
            eventDate = request.eventDate,
            startTime = request.startTime,
            endTime = request.endTime,
            location = request.location,
            createdBy = user.toAccount(),
        )

        return eventRepository.save(event).toResponse()
    }

    fun getAllEvents(page: Int, size: Int): PageResponse<EventResponse> {
        val pageable: Pageable = PageRequest.of(page, size)
        val eventPage = eventRepository.findAll(pageable)
        return toPageResponse(eventPage) { it.toResponse()}
    }


    fun getEventById(eventId: Long): EventResponse =
        eventRepository.findById(eventId)
            .orElseThrow { IllegalArgumentException("Event not found") }
            .toResponse()

    @Transactional
    fun updateEvent(eventId: Long, request: EventRequest): EventResponse {
        val event = eventRepository.findById(eventId)
            .orElseThrow { IllegalArgumentException("Event not found") }

        val updated = event.copy(
            title = request.title,
            description = request.description,
            eventDate = request.eventDate,
            startTime = request.startTime,
            endTime = request.endTime,
            location = request.location
        )

        return eventRepository.save(updated).toResponse()
    }

    @Transactional
    fun deleteEvent(eventId: Long) {
        val event = eventRepository.findById(eventId)
            .orElseThrow { IllegalArgumentException("Event not found") }
        eventRepository.delete(event)
    }

    @Transactional
    fun rsvp(request: EventAttendanceRequest): EventAttendanceResponse {
        val event = eventRepository.findById(request.eventId)
            .orElseThrow { IllegalArgumentException("Event not found") }
        val user = userRepository.findById(request.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val status = AttendanceStatus.valueOf(request.status.uppercase())

        val existing = eventAttendanceRepository
            .findByEventId(event.id)
            .firstOrNull { it.user.id == user.id }

        val attendance = existing?.copy(status = status)
            ?: EventAttendance(event = event, user = user, status = status)

        return eventAttendanceRepository.save(attendance).toResponse()
    }

    fun getAttendancesForEvent(eventId: Long): List<EventAttendanceResponse> =
        eventAttendanceRepository.findByEventId(eventId).map { it.toResponse() }
}