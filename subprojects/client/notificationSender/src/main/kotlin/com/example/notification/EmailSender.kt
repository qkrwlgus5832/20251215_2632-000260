package com.example.notification

import com.example.notification.common.NotificationSender
import com.example.notification.properties.NotificationSenderProperties
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class EmailSender(
    private val restTemplate: RestTemplate,
    private val notificationSenderProperties: NotificationSenderProperties
) {
    companion object {
        private const val EMAIL_PATH = "/send/email"
        private const val RETRY_COUNT = 3
    }

    fun send(request: EmailRequest): ResultCode {
        return NotificationSender.send(
            request,
            EMAIL_PATH,
            RETRY_COUNT,
            restTemplate = restTemplate,
            notificationSenderProperties = notificationSenderProperties
        )
    }

    data class EmailRequest(
        val emailAddress: String,
        val title: String,
        val contents: String
    )
}