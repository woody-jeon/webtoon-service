package com.lezhin.webtoonservice.core.domain.purchase.model.repository

import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase

interface PurchaseRepository {
    fun findByIdempotencyKey(idempotencyKey: String): Purchase?

    fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): Purchase?

    fun save(purchase: Purchase): Purchase
}
