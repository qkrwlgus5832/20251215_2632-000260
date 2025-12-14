package com.example.notification.service.log

import com.example.notification.ResultCode
import com.example.notification.domain.entity.log.NotificationLog
import com.example.notification.domain.enums.NotificationStatus
import com.example.notification.domain.event.NotificationEvent
import com.example.notification.domain.repository.NotificationLogRepository
import com.example.notification.service.log.condition.NotificationLogSearchCondition
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class NotificationLogService(
    private val notificationLogRepository: NotificationLogRepository
) {
    companion object {
        private const val LOG_SEARCH_MONTH = 3L
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateNotification(event: NotificationEvent): NotificationLog? {
        val existed = notificationLogRepository.findFirstByEventId(event.eventId)

        if (existed?.status == NotificationStatus.FAIL || existed?.status == NotificationStatus.RESERVED){
            existed.markPending()
        }

        return existed
    }

    @Transactional
    fun updateNotification(event: NotificationEvent, resultCode: ResultCode): NotificationLog? {
        val existed = notificationLogRepository.findFirstByEventId(event.eventId)

        if (resultCode == ResultCode.SUCCESS) {
            existed?.markSuccess()
        }
        else {
            existed?.markFail()
        }

        return existed
    }

    @Transactional
    fun saveInstantNotification(event: NotificationEvent): NotificationLog {
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

    @Transactional
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

    @Transactional
    fun getRecentLogs(
        condition: NotificationLogSearchCondition,
        page: Int,
        size: Int,
    ): Page<NotificationLog> {
        val pageable = PageRequest.of(
            page,
            size
        )

        require(condition.from != null) {
            "시작날짜는 필수값입니다"
        }

        require(condition.from.isBefore(condition.to) || condition.from == condition.to) {
            "시작날짜는 종료날짜보다 이전이어야 합니다."
        }

        require(!condition.from.isBefore(LocalDate.now().minusMonths(LOG_SEARCH_MONTH))) {
            "조회 가능한 기간은 최근 3개월 이내입니다."
        }

        return notificationLogRepository.findRecentLogs(
            condition.requesterId,
            condition.from,
            condition.to,
            status = condition.status,
            channel = condition.channel,
            userId = condition.userId,
            pageable
        )
    }

}