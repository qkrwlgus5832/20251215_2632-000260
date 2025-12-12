package com.example.notification.domain.event

import java.time.LocalDateTime
import java.util.UUID

enum class Channel {
    KAKAO,
    SMS,
    EMAIL,
}

data class NotificationEvent(
    val eventId: String = UUID.randomUUID().toString(),
    val channel: Channel,
    val title: String,
    val contents: String,
    val target: String,
    val requestedAt: LocalDateTime = LocalDateTime.now()
)