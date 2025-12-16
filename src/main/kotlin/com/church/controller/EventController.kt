package com.church.controller

import com.church.payload.event.EventAttendanceRequest
import com.church.payload.event.EventAttendanceResponse
import com.church.payload.event.EventRequest
import com.church.payload.event.EventResponse
import com.church.payload.pagination.PageResponse
import com.church.security.User
import com.church.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @PostMapping
    fun createEvent(
        @AuthenticationPrincipal user: User,
        @RequestBody request: EventRequest): ResponseEntity<EventResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(user,request))

    @GetMapping
    fun getAllEvents(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<PageResponse<EventResponse>> =
        ResponseEntity.ok(eventService.getAllEvents(page,size))

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventResponse> =
        ResponseEntity.ok(eventService.getEventById(id))

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @PutMapping("/{id}")
    fun updateEvent(
        @AuthenticationPrincipal user: User,
        @PathVariable id: Long,
        @RequestBody request: EventRequest
    ): ResponseEntity<EventResponse> =
        ResponseEntity.ok(eventService.updateEvent(id, request))

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteEvent(@PathVariable id: Long): ResponseEntity<Void> {
        eventService.deleteEvent(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{id}/rsvp")
    fun rsvp(
        @PathVariable id: Long,
        @RequestBody request: EventAttendanceRequest
    ): ResponseEntity<EventAttendanceResponse> =
        ResponseEntity.ok(eventService.rsvp(request.copy(eventId = id)))


    @GetMapping("/{id}/attendances")
    fun getAttendancesForEvent(@PathVariable id: Long): ResponseEntity<List<EventAttendanceResponse>> =
        ResponseEntity.ok(eventService.getAttendancesForEvent(id))
}