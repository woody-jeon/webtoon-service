package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisodeAccess
import com.lezhin.webtoonservice.core.domain.episode.model.repository.UserEpisodeAccessRepository
import com.lezhin.webtoonservice.storage.db.core.episode.entity.UserEpisodeAccessEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class UserEpisodeAccessEntityRepository(
    private val jpaRepository: UserEpisodeAccessEntityJpaRepository,
    private val queryRepository: UserEpisodeAccessEntityQueryRepository,
) : UserEpisodeAccessRepository {
    @Transactional(readOnly = true)
    override fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): UserEpisodeAccess? = jpaRepository.findByUserIdAndEpisodeId(userId, episodeId)?.toDomain()

    override fun save(
        userId: Long,
        episodeId: Long,
        purchaseId: Long,
    ) {
        val entity =
            UserEpisodeAccessEntity(
                userId = userId,
                episodeId = episodeId,
                purchaseId = purchaseId,
            )
        jpaRepository.save(entity)
    }

    override fun updateLastAccessedAt(
        userId: Long,
        episodeId: Long,
    ) {
        jpaRepository.findByUserIdAndEpisodeId(userId, episodeId)?.updateLastAccessedAt()
    }
}
