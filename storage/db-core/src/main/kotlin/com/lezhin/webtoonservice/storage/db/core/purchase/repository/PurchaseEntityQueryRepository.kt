package com.lezhin.webtoonservice.storage.db.core.purchase.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class PurchaseEntityQueryRepository(
    private val queryFactory: JPAQueryFactory,
)
