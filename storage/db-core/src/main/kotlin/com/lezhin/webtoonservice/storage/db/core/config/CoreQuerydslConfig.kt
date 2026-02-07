package com.lezhin.webtoonservice.storage.db.core.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoreQuerydslConfig(
    val em: EntityManager,
) {
    @Bean(name = ["coreQuerydslFactory"])
    fun queryFactory(): JPAQueryFactory = JPAQueryFactory(em)
}
