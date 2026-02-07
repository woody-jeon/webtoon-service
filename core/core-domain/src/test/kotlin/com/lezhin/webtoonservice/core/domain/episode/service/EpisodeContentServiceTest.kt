package com.lezhin.webtoonservice.core.domain.episode.service

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisode
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisodeAccess
import com.lezhin.webtoonservice.core.domain.episode.service.function.EpisodeFinder
import com.lezhin.webtoonservice.core.domain.episode.service.function.UserEpisodeAccessModifier
import com.lezhin.webtoonservice.core.domain.episode.service.function.UserEpisodeAccessValidator
import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.domain.user.service.function.UserFinder
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class EpisodeContentServiceTest {
    private lateinit var userFinder: UserFinder
    private lateinit var episodeFinder: EpisodeFinder
    private lateinit var userEpisodeAccessValidator: UserEpisodeAccessValidator
    private lateinit var userEpisodeAccessModifier: UserEpisodeAccessModifier
    private lateinit var episodeContentService: EpisodeContentService

    @BeforeEach
    fun setUp() {
        userFinder = mockk()
        episodeFinder = mockk()
        userEpisodeAccessValidator = mockk()
        userEpisodeAccessModifier = mockk()

        episodeContentService =
            EpisodeContentService(
                userFinder,
                episodeFinder,
                userEpisodeAccessValidator,
                userEpisodeAccessModifier,
            )
    }

    @Test
    @DisplayName("구매한 회차의 콘텐츠를 정상적으로 조회한다")
    fun testGetEpisodeContentSuccess() {
        // Given
        val userId = UserId(1L)
        val episodeId = EpisodeId(1L)
        val userEpisode = UserEpisode(userId, episodeId)

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

        val userEpisodeAccess =
            UserEpisodeAccess(
                id = 1L,
                userId = 1L,
                episodeId = 1L,
                purchaseId = 1L,
                grantedAt = LocalDateTime.now(),
                expiresAt = null,
                lastAccessedAt = null,
            )

        // Mock 설정
        every { userEpisodeAccessValidator.validateAndGetAccess(userId, episodeId) } returns userEpisodeAccess
        every { userFinder.findById(userId) } returns user
        every { episodeFinder.findById(episodeId) } returns episode
        justRun { userEpisodeAccessModifier.modifyLastAccessedAt(userId, episodeId) }

        // When
        val result = episodeContentService.getEpisodeContent(userEpisode)

        // Then
        assertEquals(episode.id, result.id)
        assertEquals(episode.title, result.title)
        assertEquals(episode.price, result.price)
    }
}