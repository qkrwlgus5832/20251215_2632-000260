package com.example.notification.scheduler

import com.example.notification.ResultCode
import com.example.notification.domain.extension.toEvent
import com.example.notification.domain.repository.NotificationLogRepository
import com.example.notification.service.NotificationPublisher
import com.example.notification.service.NotificationServerSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * 실패한 알람들을 30분마다 재시도 해주는 scheduler
 */

@Component
class FailedNotificationRetryScheduler(
    private val logRepository: NotificationLogRepository,
    private val notificationPublisher: NotificationPublisher
) {

    @Transactional
    @Scheduled(cron = "0 */30 * * * *") // 30분마다 정각
    fun retryFailedNotifications() {
        val now = LocalDateTime.now()

        val logs = logRepository.findReservableLogs(now)

        for (log in logs) {
            notificationPublisher.publish(log.toEvent())
        }
    }
}