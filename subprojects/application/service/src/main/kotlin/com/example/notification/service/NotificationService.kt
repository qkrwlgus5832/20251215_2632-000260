package com.example.notification.service

import com.example.notification.domain.event.NotificationEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotificationService(
    private val notificationPublisher: NotificationPublisher
) {
    fun sendNotificationEvent(event: NotificationEvent, reserveTime: LocalDateTime?) {
        if (reserveTime == null) { // 즉시 전송이라면
            notificationPublisher.publish(event) // 즉시 Kafka 메시지 발행
        }
    }
}