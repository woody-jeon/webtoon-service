package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisodeAccess
import com.lezhin.webtoonservice.core.domain.episode.model.repository.UserEpisodeAccessRepository
import com.lezhin.webtoonservice.storage.db.core.episode.entity.UserEpisodeAccessEntity
import org.springframework.stereotype.Repository

@Repository
class UserEpisodeAccessEntityRepository(
    private val accessJpaRepository: UserEpisodeAccessEntityJpaRepository,
) : UserEpisodeAccessRepository {
    override fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): UserEpisodeAccess? {
        return accessJpaRepository.findByUserIdAndEpisodeId(userId, episodeId)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun save(access: UserEpisodeAccess): UserEpisodeAccess {
        val entity = UserEpisodeAccessEntity.from(access)
        val savedEntity = accessJpaRepository.save(entity)
        return savedEntity.toDomain()
    }

    override fun updateLastAccessedAt(
        userId: Long,
        episodeId: Long,
    ) {
        accessJpaRepository.updateLastAccessedAt(userId, episodeId)
    }
}
