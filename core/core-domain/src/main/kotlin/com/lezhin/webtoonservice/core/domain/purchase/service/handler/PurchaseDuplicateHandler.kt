package com.lezhin.webtoonservice.core.domain.purchase.service.handler

import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.purchase.model.IdempotencyKey
import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.service.function.PurchaseFinder
import com.lezhin.webtoonservice.core.domain.support.error.CoreDomainException
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorType
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class PurchaseDuplicateHandler(
    private val purchaseFinder: PurchaseFinder,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    fun handleDuplicate(
        userId: UserId,
        episodeId: EpisodeId,
        idempotencyKey: IdempotencyKey,
    ): Purchase {
        // 같은 idempotencyKey로 저장된 경우
        purchaseFinder.findByIdempotencyKeyAndNotExpired(idempotencyKey = idempotencyKey)?.let { return it }

        // userId, episodeId로 완료 데이터 있으면 다른 요청으로 이미 구매
        purchaseFinder
            .findByUserIdAndEpisodeIdAndStatus(
                userId = userId,
                episodeId = episodeId,
                status = PurchaseStatus.COMPLETED,
            )?.let {
                throw CoreDomainException(DomainErrorType.WEBTOON_ALREADY_PURCHASED)
            }
        throw CoreDomainException(DomainErrorType.PURCHASE_FAILED)
    }
}
