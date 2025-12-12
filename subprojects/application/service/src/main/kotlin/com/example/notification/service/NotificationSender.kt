package com.example.notification.service

import com.example.notification.EmailSender
import com.example.notification.KakaoTalkSender
import com.example.notification.ResultCode
import com.example.notification.SmsSender
import com.example.notification.domain.event.Channel
import com.example.notification.domain.event.NotificationEvent
import org.springframework.stereotype.Service

@Service
class NotificationSender(
    private val emailSender: EmailSender,
    private val kakaoTalkSender: KakaoTalkSender,
    private val smsSender: SmsSender
) {
    fun send(event: NotificationEvent) {
        when (event.channel) {
            Channel.SMS -> sendSms(event)
            Channel.KAKAO -> sendKakao(event)
            Channel.EMAIL -> sendEmail(event)
        }
    }

    private fun sendSms(event: NotificationEvent): ResultCode {
        return smsSender.send(
            SmsSender.SmsRequest(
                event.target,
                event.title,
                event.contents
            )
        )
    }

    private fun sendKakao(event: NotificationEvent): ResultCode {
        return kakaoTalkSender.send(
            KakaoTalkSender.KakaoTalkRequest(
                event.target,
                event.title,
                event.contents
            )
        )
    }

    private fun sendEmail(event: NotificationEvent): ResultCode {
        return emailSender.send(
            EmailSender.EmailRequest(
                event.target,
                event.title,
                event.contents
            )
        )
    }
}