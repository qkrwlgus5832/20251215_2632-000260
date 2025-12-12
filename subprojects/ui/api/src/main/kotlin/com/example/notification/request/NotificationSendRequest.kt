package com.example.notification.request

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class NotificationSendRequest (
    val channel: Channel,
    val title: String,
    val message: String,
    val target: String,
    val requesterId: String,
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyyMMddhhmm")
    val reserveTime: LocalDateTime?
) {
    enum class Channel {
        KAKAO,
        SMS,
        EMAIL,
    }
}