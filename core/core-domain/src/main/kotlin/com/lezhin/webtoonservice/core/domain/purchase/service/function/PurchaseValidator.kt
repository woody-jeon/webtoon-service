package com.lezhin.webtoonservice.core.domain.purchase.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisode
import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.model.repository.PurchaseRepository
import com.lezhin.webtoonservice.core.domain.support.error.CoreDomainException
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorType
import com.lezhin.webtoonservice.core.domain.user.model.User
import org.springframework.stereotype.Component

@Component
class PurchaseValidator(
    private val purchaseRepository: PurchaseRepository,
) {
    fun validateIdempotencyKeyConsistency(
        userEpisode: UserEpisode,
        purchase: Purchase?,
    ) {
        if (purchase != null && (purchase.userId != userEpisode.userId.id || purchase.episodeId != userEpisode.episodeId.id)) {
            throw CoreDomainException(DomainErrorType.WEBTOON_IDEMPOTENCY_KEY_MISMATCH)
        }
    }

    fun validateNotAlreadyPurchased(userEpisode: UserEpisode) {
        val existingPurchase =
            purchaseRepository.findByUserIdAndEpisodeId(
                userId = userEpisode.userId.id,
                episodeId = userEpisode.episodeId.id,
            )
        if (existingPurchase != null) {
            throw CoreDomainException(DomainErrorType.WEBTOON_ALREADY_PURCHASED)
        }
    }

    fun validateSufficientCoin(
        user: User,
        episode: Episode,
    ) {
        if (!user.hasEnoughCoin(episode.price)) {
            throw CoreDomainException(DomainErrorType.WEBTOON_INSUFFICIENT_COIN)
        }
    }
}
