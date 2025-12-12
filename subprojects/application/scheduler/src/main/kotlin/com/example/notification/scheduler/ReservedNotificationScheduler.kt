package com.example.notification.scheduler

import com.example.notification.ResultCode
import com.example.notification.domain.extension.toEvent
import com.example.notification.domain.repository.NotificationLogRepository
import com.example.notification.service.NotificationPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class ReservedNotificationScheduler(
    private val logRepository: NotificationLogRepository,
    private val notificationPublisher: NotificationPublisher
) {
    @Transactional
    @Scheduled(cron = "0 * * * * *")
    fun sendReservedNotifications() {
        val now = LocalDateTime.now()

        val logs = logRepository.findReservableLogs(now)

        for (log in logs) {
            notificationPublisher.publish(log.toEvent())
        }
    }
}