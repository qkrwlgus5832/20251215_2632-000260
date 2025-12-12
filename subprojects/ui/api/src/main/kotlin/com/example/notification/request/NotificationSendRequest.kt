package com.example.notification.request

enum class Channel {
    KAKAO,
    SMS,
    EMAIL,
}

data class NotificationSendRequest (
    val channel: Channel,
    val title: String,
    val message: String,
    val target: String, // phone / email / userId 등
)