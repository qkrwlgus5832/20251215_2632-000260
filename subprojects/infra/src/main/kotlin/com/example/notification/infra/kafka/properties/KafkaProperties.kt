package com.example.notification.infra.kafka.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.kafka")
data class KafkaProperties(
    val bootstrapServers: String,
    val notification: Notification
) {
    data class Notification(
        val topic: String,
        val consumer: Consumer
    )

    data class Consumer(
        val groupId: String,
        val autoOffsetReset: String,
        val concurrencyCount: Int
    )
}