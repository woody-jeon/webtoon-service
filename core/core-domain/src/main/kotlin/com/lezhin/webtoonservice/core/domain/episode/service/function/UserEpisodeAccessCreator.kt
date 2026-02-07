package com.lezhin.webtoonservice.core.domain.episode.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.repository.UserEpisodeAccessRepository
import com.lezhin.webtoonservice.core.domain.purchase.model.PurchaseId
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import org.springframework.stereotype.Component

@Component
class UserEpisodeAccessCreator(
    private val userEpisodeAccessRepository: UserEpisodeAccessRepository,
) {
    fun create(
        userId: UserId,
        episodeId: EpisodeId,
        purchaseId: PurchaseId,
    ) {
        userEpisodeAccessRepository.save(
            userId = userId.id,
            episodeId = episodeId.id,
            purchaseId = purchaseId.id,
        )
    }
}
