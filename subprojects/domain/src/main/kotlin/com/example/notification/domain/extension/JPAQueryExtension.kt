package com.example.notification.domain.extension

import com.querydsl.jpa.JPQLQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.Querydsl

/**
 * Querydsl 기반 JPQLQuery에서 반복되는 페이징 로직을 공통화하기 위한 확장 함수.
 *
 * Pageable을 적용한 결과 목록과 total count를 조회해
 * Spring Data Page 형태로 반환한다.
 */
fun <T> JPQLQuery<T>.fetchPage(pageable: Pageable, querydsl: Querydsl?): Page<T> {
    return querydsl!!.applyPagination(pageable, this).fetchResults().run {
        PageImpl(results, pageable, total)
    }
}