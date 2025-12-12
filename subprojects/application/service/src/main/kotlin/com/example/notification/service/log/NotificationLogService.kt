package com.example.notification.service.log

import com.example.notification.domain.entity.log.NotificationLog
import com.example.notification.domain.enums.NotificationStatus
import com.example.notification.domain.event.NotificationEvent
import com.example.notification.domain.repository.NotificationLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class NotificationLogService(
    private val notificationLogRepository: NotificationLogRepository
) {
    fun persistNotification(event: NotificationEvent): NotificationLog {
        val existed = notificationLogRepository.findFirstByEventId(event.eventId)

        if (existed == null) {
            val log = NotificationLog(
                userId = event.target,
                requesterId = event.requesterId,
                channel = event.channel,
                status = NotificationStatus.PENDING,
                sendAt = LocalDateTime.now(),
                eventId = event.eventId,
                title = event.title,
                contents = event.contents,
                retryCount = 0
            )

            return notificationLogRepository.save(log)
        }
        else {
            if (existed.status == NotificationStatus.FAIL) {
                existed.retryCount++
            }
            existed.markPending()
        }

        return existed
    }

    fun saveReserveNotification(event: NotificationEvent): NotificationLog {
        val log = NotificationLog(
            userId = event.target,
            requesterId = event.requesterId,
            channel = event.channel,
            status = NotificationStatus.RESERVED,
            sendAt = event.reserveTime,
            eventId = event.eventId,
            title = event.title,
            contents = event.contents,
            retryCount = 0
        )

        return notificationLogRepository.save(log)
    }
}