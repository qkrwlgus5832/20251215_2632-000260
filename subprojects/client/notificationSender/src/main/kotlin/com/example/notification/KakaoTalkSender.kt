package com.example.notification

import com.example.notification.common.NotificationSender
import com.example.notification.properties.NotificationSenderProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoTalkSender(
    private val restTemplate: RestTemplate,
    private val notificationSenderProperties: NotificationSenderProperties
) {
    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val KAKAO_TALK_PATH = "/send/kakaotalk"
        private const val RETRY_COUNT = 3
    }

    fun send(
        request: KakaoTalkRequest,
    ): ResultCode {
        return NotificationSender.send(
            request,
            KAKAO_TALK_PATH,
            RETRY_COUNT,
            restTemplate = restTemplate,
            notificationSenderProperties = notificationSenderProperties
        )
    }

    data class KakaoTalkRequest(
        val talkId: String,
        val title: String,
        val contents: String
    )
}