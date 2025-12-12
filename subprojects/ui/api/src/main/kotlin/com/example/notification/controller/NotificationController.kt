package com.example.notification.controller


import com.example.notification.factory.NotificationEventFactory
import com.example.notification.service.NotificationPublisher
import com.example.notification.request.NotificationSendRequest
import com.example.notification.service.log.NotificationLogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationPublisher: NotificationPublisher,
    private val notificationLogService: NotificationLogService
) {
    @PostMapping
    fun send(
        @RequestBody request: NotificationSendRequest
    ): ResponseEntity<Void> {
        val event = NotificationEventFactory.from(request)

        if (request.reserveTime == null) {
            notificationPublisher.publish(event)
        } else {
            notificationLogService.saveReserveNotification(event)
        }
        return ResponseEntity.accepted().build()
    }
}