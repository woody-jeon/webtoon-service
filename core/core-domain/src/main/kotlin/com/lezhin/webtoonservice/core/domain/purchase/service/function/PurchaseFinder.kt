package com.lezhin.webtoonservice.core.domain.purchase.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.purchase.model.IdempotencyKey
import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.model.repository.PurchaseRepository
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import org.springframework.stereotype.Component

@Component
class PurchaseFinder(
    private val purchaseRepository: PurchaseRepository,
) {
    fun findByIdempotencyKeyAndNotExpired(idempotencyKey: IdempotencyKey): Purchase? =
        purchaseRepository.findByIdempotencyKeyAndNotExpired(idempotencyKey = idempotencyKey.key)

    fun findByUserIdAndEpisodeIdAndStatus(
        userId: UserId,
        episodeId: EpisodeId,
        status: PurchaseStatus,
    ): Purchase? = purchaseRepository.findByUserIdAndEpisodeIdAndStatus(userId.id, episodeId.id, status)
}
