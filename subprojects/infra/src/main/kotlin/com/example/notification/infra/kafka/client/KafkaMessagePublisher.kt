package com.example.notification.infra.kafka.client

interface KafkaMessagePublisher {
    fun<T> publish(topic: String, key: String, message: T)
}