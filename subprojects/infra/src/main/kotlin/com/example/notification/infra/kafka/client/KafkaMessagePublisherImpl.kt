package com.example.notification.infra.kafka.client

import org.springframework.kafka.core.KafkaTemplate
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class KafkaMessagePublisherImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) : KafkaMessagePublisher {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun<T> publish(topic: String, key: String, message: T) {
        try {
            val result = kafkaTemplate.send(topic, key, message).get(30, TimeUnit.SECONDS)
            log.info("Published offset={}", result.recordMetadata.offset())
        } catch(ex: Exception) {
            log.error("Kafka publish failed", ex)
            throw ex
        }
    }
}