package com.church.controller

import com.church.payload.event.EventAttendanceRequest
import com.church.payload.event.EventAttendanceResponse
import com.church.payload.event.EventRequest
import com.church.payload.event.EventResponse
import com.church.service.EventService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api/events")
class EventController(
    private val eventService: EventService
) {

    @PreAuthorize("hasAnyRole('PASTOR','ADMIN')")
    @PostMapping
    fun createEvent(@RequestBody request: EventRequest): ResponseEntity<EventResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request))

    @GetMapping
    fun getAllEvents(): ResponseEntity<List<EventResponse>> =
        ResponseEntity.ok(eventService.getAllEvents())

    @GetMapping("/{id}")
    fun getEventById(@PathVariable id: Long): ResponseEntity<EventResponse> =
        ResponseEntity.ok(eventService.getEventById(id))

    @PutMapping("/{id}")
    fun updateEvent(
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