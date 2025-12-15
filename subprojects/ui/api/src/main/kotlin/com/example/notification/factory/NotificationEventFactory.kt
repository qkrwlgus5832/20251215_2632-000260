package com.example.notification.factory

import com.example.notification.domain.enums.Channel
import com.example.notification.domain.event.NotificationEvent
import com.example.notification.request.NotificationSendRequest

object NotificationEventFactory {

    fun from(request: NotificationSendRequest): NotificationEvent {
        return NotificationEvent(
            channel = Channel.valueOf(request.channel.name),
            title = request.title,
            contents = request.message,
            target = request.target,
            requesterId = request.requesterId,
        ).apply {
            request.reserveTime?.let {
                this.reserveTime = it
            }
        }

    }
}