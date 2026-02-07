package com.lezhin.webtoonservice.core.domain.purchase.service

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
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
import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.domain.user.service.function.UserFinder
import com.lezhin.webtoonservice.core.domain.user.service.function.UserModifier
import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class EpisodePurchaseServiceTest {
    private lateinit var userFinder: UserFinder
    private lateinit var episodeFinder: EpisodeFinder
    private lateinit var purchaseFinder: PurchaseFinder
    private lateinit var purchaseValidator: PurchaseValidator
    private lateinit var purchaseCreator: PurchaseCreator
    private lateinit var userModifier: UserModifier
    private lateinit var userEpisodeAccessCreator: UserEpisodeAccessCreator
    private lateinit var purchaseDuplicateHandler: PurchaseDuplicateHandler
    private lateinit var episodePurchaseService: EpisodePurchaseService

    @BeforeEach
    fun setUp() {
        userFinder = mockk()
        episodeFinder = mockk()
        purchaseFinder = mockk()
        purchaseValidator = mockk()
        purchaseCreator = mockk()
        userModifier = mockk()
        userEpisodeAccessCreator = mockk()
        purchaseDuplicateHandler = mockk()

        episodePurchaseService =
            EpisodePurchaseService(
                userFinder,
                episodeFinder,
                purchaseFinder,
                purchaseValidator,
                purchaseCreator,
                userModifier,
                userEpisodeAccessCreator,
                purchaseDuplicateHandler,
            )
    }

    @Test
    @DisplayName("정상적인 회차 구매가 성공한다")
    fun testPurchaseEpisodeSuccess() {
        // Given
        val userId = UserId(1L)
        val episodeId = EpisodeId(1L)
        val userEpisode = UserEpisode(userId, episodeId)
        val idempotencyKey = IdempotencyKey("test-idempotency-key")

        val user =
            User(
                id = 1L,
                coin = BigDecimal("1000.00"),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )

        val episode =
            Episode(
                id = 1L,
                webtoonId = 1L,
                episodeNumber = 1,
                title = "1화",
                price = BigDecimal("300.00"),
                thumbnail = "https://example.com/thumb.jpg",
                content = mockk(relaxed = true),
                isAvailable = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )

        val purchase =
            Purchase(
                id = 1L,
                userId = 1L,
                episodeId = 1L,
                idempotencyKey = "test-idempotency-key",
                idempotencyExpiresAt = LocalDateTime.now().plusMinutes(1),
                amount = BigDecimal("300.00"),
                status = PurchaseStatus.COMPLETED,
                createdAt = LocalDateTime.now(),
                completedAt = LocalDateTime.now(),
            )

        // Mock 설정
        every { purchaseFinder.findByIdempotencyKeyAndNotExpired(idempotencyKey) } returns null
        justRun { purchaseValidator.validateIdempotencyKeyConsistency(userEpisode, null) }
        justRun { purchaseValidator.validateNotAlreadyPurchased(userEpisode) }
        every { userFinder.findByIdWithLock(userId) } returns user
        every { episodeFinder.findAvailableEpisodeById(episodeId) } returns episode
        justRun { purchaseValidator.validateSufficientCoin(user, episode) }
        justRun { userModifier.decrementCoin(user, episode) }
        every { purchaseCreator.createPurchase(userId, episode, idempotencyKey) } returns purchase
        justRun { userEpisodeAccessCreator.create(userId, episodeId, PurchaseId(purchase.id)) }

        // When
        val result = episodePurchaseService.purchaseEpisode(userEpisode, idempotencyKey)

        // Then
        assertEquals(purchase.id, result.id)
        assertEquals(purchase.userId, result.userId)
        assertEquals(purchase.episodeId, result.episodeId)
        assertEquals(PurchaseStatus.COMPLETED, result.status)
    }
}