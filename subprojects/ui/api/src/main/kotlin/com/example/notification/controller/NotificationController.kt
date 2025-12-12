package com.example.notification.controller


import com.example.notification.factory.NotificationEventFactory
import com.example.notification.service.NotificationPublisher
import com.example.notification.request.NotificationSendRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationPublisher: NotificationPublisher
) {

    @PostMapping
    fun send(
        @RequestBody request: NotificationSendRequest
    ): ResponseEntity<Void> {
        val event = NotificationEventFactory.from(request)
        notificationPublisher.publish(event)
        return ResponseEntity.accepted().build()
    }
}