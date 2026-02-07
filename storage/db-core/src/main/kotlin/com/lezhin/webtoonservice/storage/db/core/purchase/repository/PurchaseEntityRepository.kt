package com.lezhin.webtoonservice.storage.db.core.purchase.repository

import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.model.repository.PurchaseRepository
import com.lezhin.webtoonservice.storage.db.core.purchase.entity.PurchaseEntity
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class PurchaseEntityRepository(
    private val purchaseEntityJpaRepository: PurchaseEntityJpaRepository,
) : PurchaseRepository {
    override fun findByIdempotencyKey(idempotencyKey: String): Purchase? {
        return purchaseEntityJpaRepository
            .findByIdempotencyKeyAndIdempotencyExpiresAtGreaterThan(idempotencyKey, LocalDateTime.now())
            ?.toDomain()
    }

    override fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): Purchase? =
        purchaseEntityJpaRepository
            .findByUserIdAndEpisodeId(userId, episodeId)
            ?.toDomain()

    override fun save(purchase: Purchase): Purchase {
        val entity = PurchaseEntity.from(purchase)
        val savedEntity = purchaseEntityJpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}
