package com.example.notification.domain.enums

enum class Channel(val partition: Int) {
    KAKAO(0),
    SMS(1),
    EMAIL(2),
}