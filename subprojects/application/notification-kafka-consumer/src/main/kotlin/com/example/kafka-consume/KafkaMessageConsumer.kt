package com.example.`kafka-consume`

import com.example.notification.domain.event.NotificationEvent
import org.springframework.kafka.support.Acknowledgment

interface KafkaMessageConsumer {
    fun consume(
        event: NotificationEvent,
        acknowledgement: Acknowledgment
    )
}