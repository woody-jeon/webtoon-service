package com.lezhin.webtoonservice.storage.db.core.purchase.repository

import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.model.repository.PurchaseRepository
import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import com.lezhin.webtoonservice.storage.db.core.purchase.entity.PurchaseEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Repository
@Transactional
class PurchaseEntityRepository(
    private val jpaRepository: PurchaseEntityJpaRepository,
    private val queryRepository: PurchaseEntityQueryRepository,
) : PurchaseRepository {
    companion object {
        private const val IDEMPOTENCY_TTL_MINUTES = 1L
    }

    @Transactional(readOnly = true)
    override fun findByIdempotencyKeyAndNotExpired(idempotencyKey: String): Purchase? =
        jpaRepository
            .findByIdempotencyKeyAndIdempotencyExpiresAtGreaterThan(
                idempotencyKey = idempotencyKey,
                now = LocalDateTime.now(),
            )?.toDomain()

    @Transactional(readOnly = true)
    override fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): Purchase? =
        jpaRepository
            .findByUserIdAndEpisodeId(userId, episodeId)
            ?.toDomain()

    @Transactional(readOnly = true)
    override fun findByUserIdAndEpisodeIdAndStatus(
        userId: Long,
        episodeId: Long,
        status: PurchaseStatus,
    ): Purchase? =
        jpaRepository
            .findByUserIdAndEpisodeIdAndStatus(userId, episodeId, status)
            ?.toDomain()

    override fun save(
        userId: Long,
        episodeId: Long,
        idempotencyKey: String,
        amount: BigDecimal,
    ): Purchase {
        val now = LocalDateTime.now()
        val entity =
            PurchaseEntity(
                userId = userId,
                episodeId = episodeId,
                idempotencyKey = idempotencyKey,
                idempotencyExpiresAt = now.plusMinutes(IDEMPOTENCY_TTL_MINUTES),
                amount = amount,
                status = PurchaseStatus.COMPLETED,
                completedAt = now,
            )
        val savedEntity = jpaRepository.save(entity)
        return savedEntity.toDomain()
    }
}
