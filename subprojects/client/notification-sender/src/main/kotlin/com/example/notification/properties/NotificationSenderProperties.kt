package com.example.notification.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "notification-sender")
data class NotificationSenderProperties(
    val server: Server
) {
    data class Server(
        val host: String,
        val port: Int
    ) {
        val baseUrl: String
            get() = "http://$host:$port"
    }
}