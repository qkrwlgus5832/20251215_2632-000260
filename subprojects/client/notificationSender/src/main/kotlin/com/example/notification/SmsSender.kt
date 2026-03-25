package com.example.notification

import com.example.notification.common.NotificationSender
import com.example.notification.properties.NotificationSenderProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class SmsSender (
    private val webClient: WebClient,
    private val notificationSenderProperties: NotificationSenderProperties
) {
    companion object {
        private const val SMS_PATH = "/send/sms"
        private const val RETRY_COUNT = 3
    }

    fun send(request: SmsRequest, retryCount: Int = 0): ResultCode {
        return NotificationSender.send(
            request,
            SMS_PATH,
            RETRY_COUNT,
            webClient = webClient,
            notificationSenderProperties = notificationSenderProperties
        )
    }

    data class SmsRequest(
        val phoneNumber: String,
        val title: String,
        val contents: String
    )
}