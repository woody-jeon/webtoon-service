package com.lezhin.webtoonservice.storage.db.core.user.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserEntityQueryRepository(
    private val queryFactory: JPAQueryFactory,
)
