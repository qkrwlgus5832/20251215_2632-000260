package com.example.notification.common

import com.example.notification.ResultCode
import com.example.notification.SendResponse
import com.example.notification.properties.NotificationSenderProperties
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient

/**
 * 외부 알림 서버로 전송 요청을 보내고,
 * 성공할 때까지 지정된 횟수만큼 재시도한 뒤 결과를 반환한다.
 *
 * - HTTP 요청은 동기(blocking) 방식으로 수행된다.
 * - 서버 오류 또는 예외 발생 시 delayMillis 간격으로 재시도한다.
 * - SUCCESS 응답을 받으면 즉시 종료한다.
 */
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