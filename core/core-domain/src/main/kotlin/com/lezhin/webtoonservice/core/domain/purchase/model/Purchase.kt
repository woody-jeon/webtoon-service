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
)
