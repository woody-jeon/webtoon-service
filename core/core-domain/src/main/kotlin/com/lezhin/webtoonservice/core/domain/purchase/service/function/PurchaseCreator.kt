package com.lezhin.webtoonservice.core.domain.purchase.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.purchase.model.IdempotencyKey
import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.model.repository.PurchaseRepository
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import org.springframework.stereotype.Component

@Component
class PurchaseCreator(
    private val purchaseRepository: PurchaseRepository,
) {
    fun createPurchase(
        userId: UserId,
        episode: Episode,
        idempotencyKey: IdempotencyKey,
    ): Purchase =
        purchaseRepository.save(
            userId = userId.id,
            episodeId = episode.id,
            idempotencyKey = idempotencyKey.key,
            amount = episode.price,
        )
}
