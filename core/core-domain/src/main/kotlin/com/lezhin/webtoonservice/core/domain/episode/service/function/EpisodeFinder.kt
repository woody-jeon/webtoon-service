package com.lezhin.webtoonservice.core.domain.episode.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.repository.EpisodeRepository
import com.lezhin.webtoonservice.core.domain.support.error.CoreDomainException
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorType
import org.springframework.stereotype.Component

@Component
class EpisodeFinder(
    private val episodeRepository: EpisodeRepository,
) {
    fun findById(episodeId: EpisodeId): Episode =
        episodeRepository.findById(episodeId.id)
            ?: throw CoreDomainException(DomainErrorType.EPISODE_NOT_FOUND)

    fun findAvailableEpisodeById(episodeId: EpisodeId): Episode {
        val episode = findById(episodeId)
        if (!episode.canBePurchased()) {
            throw CoreDomainException(DomainErrorType.WEBTOON_EPISODE_NOT_AVAILABLE)
        }
        return episode
    }
}
