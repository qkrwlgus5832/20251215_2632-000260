package com.example.notification.domain.repository

import com.example.notification.domain.entity.log.NotificationLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface NotificationLogRepository : JpaRepository<NotificationLog, Long>, NotificationLogQueryDslRepository {
    fun findFirstByEventId(eventId: String): NotificationLog?

    @Query(
        """
        select l from NotificationLog l
        where l.status = 'RESERVED'
          and l.sendAt <= :now
        order by l.createdAt
    """
    )
    fun findReservableLogs(
        @Param("now") now: LocalDateTime
    ): List<NotificationLog>

    @Query(
        """
        select l
        from NotificationLog l
        where l.status = 'FAIL'
            and l.retryCount <= :maxRetry
        order by l.createdAt
    """
    )
    fun findRetryableFailedLogs(maxRetry: Int): List<NotificationLog>
}