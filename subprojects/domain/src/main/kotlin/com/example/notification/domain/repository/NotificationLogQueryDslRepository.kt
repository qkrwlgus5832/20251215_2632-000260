package com.example.notification.domain.repository

import com.querydsl.jpa.impl.JPAQueryFactory

interface NotificationLogQueryDslRepository {
}

class NotificationLogQueryDslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) {
}