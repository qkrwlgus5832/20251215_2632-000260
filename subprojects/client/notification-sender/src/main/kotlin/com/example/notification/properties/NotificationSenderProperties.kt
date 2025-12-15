package com.example.notification.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "notification-sender")
data class NotificationSenderProperties(
    val server: Server
) {
    data class Server(
        val scheme: String = "http",
        val host: String,
        val port: Int
    ) {
        val baseUrl: String
            get() = "$scheme://$host:$port"
    }
}