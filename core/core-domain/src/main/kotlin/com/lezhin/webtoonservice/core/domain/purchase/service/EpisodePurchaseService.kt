package com.lezhin.webtoonservice.core.domain.purchase.service

import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisode
import com.lezhin.webtoonservice.core.domain.episode.service.function.EpisodeFinder
import com.lezhin.webtoonservice.core.domain.episode.service.function.UserEpisodeAccessCreator
import com.lezhin.webtoonservice.core.domain.purchase.model.IdempotencyKey
import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.domain.purchase.model.PurchaseId
import com.lezhin.webtoonservice.core.domain.purchase.service.function.PurchaseCreator
import com.lezhin.webtoonservice.core.domain.purchase.service.function.PurchaseFinder
import com.lezhin.webtoonservice.core.domain.purchase.service.function.PurchaseValidator
import com.lezhin.webtoonservice.core.domain.purchase.service.handler.PurchaseDuplicateHandler
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.domain.user.service.function.UserFinder
import com.lezhin.webtoonservice.core.domain.user.service.function.UserModifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EpisodePurchaseService(
    private val userFinder: UserFinder,
    private val episodeFinder: EpisodeFinder,
    private val purchaseFinder: PurchaseFinder,
    private val purchaseValidator: PurchaseValidator,
    private val purchaseCreator: PurchaseCreator,
    private val userModifier: UserModifier,
    private val userEpisodeAccessCreator: UserEpisodeAccessCreator,
    private val purchaseDuplicateHandler: PurchaseDuplicateHandler,
) {
    @Transactional
    fun purchaseEpisode(
        userEpisode: UserEpisode,
        idempotencyKey: IdempotencyKey,
    ): Purchase {
        val findPurchase = purchaseFinder.findByIdempotencyKeyAndNotExpired(idempotencyKey)
        purchaseValidator.validateIdempotencyKeyConsistency(
            userEpisode = userEpisode,
            purchase = findPurchase,
        )
        purchaseValidator.validateNotAlreadyPurchased(userEpisode)

        val findUser = userFinder.findByIdWithLock(userEpisode.userId)
        val findEpisode = episodeFinder.findAvailableEpisodeById(userEpisode.episodeId)
        purchaseValidator.validateSufficientCoin(findUser, findEpisode)
        val userId = UserId(findUser.id)
        val episodeId = EpisodeId(findEpisode.id)

        return try {
            val savedPurchase = purchaseCreator.createPurchase(userId, findEpisode, idempotencyKey)
            userModifier.decrementCoin(findUser, findEpisode)
            userEpisodeAccessCreator.create(userId, episodeId, PurchaseId(savedPurchase.id))
            savedPurchase
        } catch (_: Exception) {
            purchaseDuplicateHandler.handleDuplicate(userId, episodeId, idempotencyKey)
        }
    }
}
