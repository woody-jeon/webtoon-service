package com.lezhin.webtoonservice.core.domain.purchase.model

import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class Purchase(
    val id: Long,
    val userId: Long,
    val episodeId: Long,
    val idempotencyKey: String,
    val idempotencyExpiresAt: LocalDateTime,
    val amount: BigDecimal,
    val status: PurchaseStatus,
    val createdAt: LocalDateTime,
    val completedAt: LocalDateTime?,
) {
    fun isCompleted(): Boolean = status == PurchaseStatus.COMPLETED

    companion object {
        private const val IDEMPOTENCY_TTL_HOURS = 24L

        fun create(
            userId: Long,
            episodeId: Long,
            idempotencyKey: String,
            amount: BigDecimal,
        ): Purchase {
            val now = LocalDateTime.now()
            return Purchase(
                id = 0,
                userId = userId,
                episodeId = episodeId,
                idempotencyKey = idempotencyKey,
                idempotencyExpiresAt = now.plusHours(IDEMPOTENCY_TTL_HOURS),
                amount = amount,
                status = PurchaseStatus.COMPLETED,
                createdAt = now,
                completedAt = now,
            )
        }
    }
}
