package com.example.notification.domain.event

import com.example.notification.domain.enums.Channel
import java.time.LocalDateTime
import java.util.UUID

data class NotificationEvent(
    val eventId: String = UUID.randomUUID().toString(),
    val channel: Channel = Channel.EMAIL,
    val title: String = "",
    val contents: String = "",
    val target: String = "",
    val requesterId: String = "",
    var reserveTime: LocalDateTime = LocalDateTime.now()
)