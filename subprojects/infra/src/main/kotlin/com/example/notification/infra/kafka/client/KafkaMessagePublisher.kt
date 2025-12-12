package com.example.notification.infra.kafka.client

interface KafkaMessagePublisher {
    fun publish(topic: String, key: String, message: String)
}