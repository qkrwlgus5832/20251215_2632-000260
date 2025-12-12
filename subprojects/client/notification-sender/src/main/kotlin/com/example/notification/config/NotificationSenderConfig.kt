package com.example.notification.config

import com.example.notification.properties.NotificationSenderProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(NotificationSenderProperties::class)
class NotificationSenderConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().build()
    }
}