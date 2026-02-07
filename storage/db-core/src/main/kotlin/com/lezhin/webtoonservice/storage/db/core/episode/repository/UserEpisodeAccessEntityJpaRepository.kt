package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.storage.db.core.episode.entity.UserEpisodeAccessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface UserEpisodeAccessEntityJpaRepository : JpaRepository<UserEpisodeAccessEntity, Long> {
    fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): Optional<UserEpisodeAccessEntity>

    @Modifying
    @Query(
        "UPDATE UserEpisodeAccessEntity a SET a.lastAccessedAt = CURRENT_TIMESTAMP WHERE a.userId = :userId AND a.episodeId = :episodeId",
    )
    fun updateLastAccessedAt(
        @Param("userId") userId: Long,
        @Param("episodeId") episodeId: Long,
    )
}
