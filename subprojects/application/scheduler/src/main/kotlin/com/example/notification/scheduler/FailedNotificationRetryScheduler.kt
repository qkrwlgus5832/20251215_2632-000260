package com.example.notification.scheduler

import com.example.notification.ResultCode
import com.example.notification.domain.extension.toEvent
import com.example.notification.domain.repository.NotificationLogRepository
import com.example.notification.service.NotificationPublisher
import com.example.notification.service.NotificationServerSender
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.time.LocalDateTime

/**
 * 실패한 알람들을 30분마다 재시도 해주는 scheduler
 */

@Component
class FailedNotificationRetryScheduler(
    private val logRepository: NotificationLogRepository,
    private val notificationPublisher: NotificationPublisher
) {
    companion object {
        private const val MAX_RETRY_COUNT = 10
    }
    @Transactional
    @Scheduled(cron = "0 */30 * * * *") // 30분마다 정각
    fun retryFailedNotifications() {
        val logs = logRepository.findRetryableFailedLogs(MAX_RETRY_COUNT)

        for (log in logs) {
            notificationPublisher.publish(log.toEvent())
        }
    }
}