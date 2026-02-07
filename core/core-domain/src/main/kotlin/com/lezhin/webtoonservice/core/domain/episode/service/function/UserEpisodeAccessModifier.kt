package com.lezhin.webtoonservice.core.domain.episode.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.repository.UserEpisodeAccessRepository
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import org.springframework.stereotype.Component

@Component
class UserEpisodeAccessModifier(
    private val userEpisodeAccessRepository: UserEpisodeAccessRepository,
) {
    fun modifyLastAccessedAt(
        userId: UserId,
        episodeId: EpisodeId,
    ) {
        userEpisodeAccessRepository.updateLastAccessedAt(
            userId = userId.id,
            episodeId = episodeId.id,
        )
    }
}
