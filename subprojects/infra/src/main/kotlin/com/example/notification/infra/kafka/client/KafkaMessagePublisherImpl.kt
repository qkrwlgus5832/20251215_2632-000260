package com.example.notification.infra.kafka.client

import com.example.notification.infra.kafka.properties.KafkaProperties
import org.springframework.kafka.core.KafkaTemplate
import org.slf4j.LoggerFactory

class KafkaMessagePublisherImpl(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val kafkaProperties: KafkaProperties
) : KafkaMessagePublisher {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun<T> publish(topic: String, key: String, message: T) {
        try {
            val result = kafkaTemplate.send(topic, key, message).get()
            log.info("Published offset={}", result.recordMetadata.offset())
        } catch(ex: Exception) {
            log.error("Kafka publish failed", ex)
            throw ex
        }
    }
}