package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.episode.model.repository.EpisodeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class EpisodeEntityRepository(
    private val jpaRepository: EpisodeEntityJpaRepository,
    private val queryRepository: EpisodeEntityQueryRepository,
) : EpisodeRepository {
    override fun findById(id: Long): Episode? = jpaRepository.findByIdOrNull(id)?.toDomain()
}
