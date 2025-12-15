package com.example.notification.service

import com.example.notification.domain.event.NotificationEvent
import com.example.notification.infra.kafka.client.KafkaMessagePublisher
import com.example.notification.infra.kafka.properties.KafkaProperties
import org.springframework.stereotype.Service

@Service
class NotificationPublisher(
    private val kafkaProperties: KafkaProperties,
    private val kafkaMessagePublisher: KafkaMessagePublisher
) {
    fun publish(event: NotificationEvent) {
        kafkaMessagePublisher.publish(
            kafkaProperties.notification.topic,
            "${event.channel}:${event.target}", // kafka 파티셔닝 키
            event
        )
    }
}