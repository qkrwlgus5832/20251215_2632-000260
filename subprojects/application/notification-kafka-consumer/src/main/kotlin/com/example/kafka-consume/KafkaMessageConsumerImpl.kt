package com.example.`kafka-consume`

import com.example.notification.domain.event.NotificationEvent
import com.example.notification.service.NotificationSender
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class KafkaMessageConsumerImpl(
    private val notificationSender: NotificationSender
): KafkaMessageConsumer {

    @KafkaListener(
        topics=["notification-topic"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    override fun consume(
        event: NotificationEvent,
        acknowledgement: Acknowledgment
    ) {
        try {
            notificationSender.send(event)
            acknowledgement.acknowledge()
        } catch (exception: Exception) {
            throw exception
        }

    }
}