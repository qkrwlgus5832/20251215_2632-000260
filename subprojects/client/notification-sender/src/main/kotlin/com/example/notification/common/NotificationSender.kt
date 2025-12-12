package com.example.notification.common

import com.example.notification.ResultCode
import com.example.notification.SendResponse
import com.example.notification.properties.NotificationSenderProperties
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient

object NotificationSender {
    fun <T : Any> send(
        request: T,
        path: String,
        maxRetry: Int = 3,
        delayMillis: Long = 1000L,
        webClient: WebClient,
        notificationSenderProperties: NotificationSenderProperties
    ): ResultCode {
        val log = LoggerFactory.getLogger(javaClass)

        repeat(maxRetry) { attempt ->
            try {
                val response = webClient.post()
                    .uri(notificationSenderProperties.server.baseUrl + path)
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(
                        { it.isError },
                        { it.createException() }
                    )
                    .bodyToMono(SendResponse::class.java)
                    .block()

                if (response?.resultCode == ResultCode.SUCCESS) {
                    return ResultCode.SUCCESS
                }
            } catch (exception: Exception) {
                log.warn("Notification send failed. retrying...", exception)
            }

            Thread.sleep(delayMillis)
        }

        return ResultCode.FAIL
    }
}