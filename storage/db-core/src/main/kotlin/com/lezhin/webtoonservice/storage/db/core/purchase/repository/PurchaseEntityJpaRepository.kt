package com.lezhin.webtoonservice.storage.db.core.purchase.repository

import com.lezhin.webtoonservice.storage.db.core.purchase.entity.PurchaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.Optional

interface PurchaseEntityJpaRepository : JpaRepository<PurchaseEntity, Long> {
    fun findByIdempotencyKeyAndIdempotencyExpiresAtGreaterThan(
        idempotencyKey: String,
        now: LocalDateTime,
    ): PurchaseEntity?

    fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): PurchaseEntity?
}
