package com.lezhin.webtoonservice.core.domain.purchase.model.repository

import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import java.math.BigDecimal

interface PurchaseRepository {
    fun findByIdempotencyKeyAndNotExpired(idempotencyKey: String): Purchase?

    fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): Purchase?

    fun findByUserIdAndEpisodeIdAndStatus(
        userId: Long,
        episodeId: Long,
        status: PurchaseStatus,
    ): Purchase?

    fun save(
        userId: Long,
        episodeId: Long,
        idempotencyKey: String,
        amount: BigDecimal,
    ): Purchase
}
