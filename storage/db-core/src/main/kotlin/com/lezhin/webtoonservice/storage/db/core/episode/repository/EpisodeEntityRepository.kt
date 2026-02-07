package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.episode.model.repository.EpisodeRepository
import org.springframework.stereotype.Repository

@Repository
class EpisodeEntityRepository(
    private val episodeEntityJpaRepository: EpisodeEntityJpaRepository,
) : EpisodeRepository {
    override fun findById(id: Long): Episode? {
        return episodeEntityJpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }
}
