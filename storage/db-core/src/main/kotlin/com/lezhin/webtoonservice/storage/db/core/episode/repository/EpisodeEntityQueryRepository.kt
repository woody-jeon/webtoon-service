package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class EpisodeEntityQueryRepository(
    private val queryFactory: JPAQueryFactory,
)
