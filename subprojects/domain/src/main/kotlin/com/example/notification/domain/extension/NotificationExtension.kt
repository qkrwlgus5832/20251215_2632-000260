package com.example.notification.domain.extension

import com.example.notification.domain.entity.log.NotificationLog
import com.example.notification.domain.event.NotificationEvent

fun NotificationLog.toEvent(): NotificationEvent {
    return NotificationEvent(
        eventId = this.eventId,
        channel = this.channel,
        title = this.title,
        contents = this.contents,
        target = this.userId,
        requesterId = this.requesterId,
        reserveTime = this.sendAt
    )
}
