package com.example.notification

data class SendResponse(
    val resultCode: ResultCode
)

enum class ResultCode {
    SUCCESS,
    FAIL
}