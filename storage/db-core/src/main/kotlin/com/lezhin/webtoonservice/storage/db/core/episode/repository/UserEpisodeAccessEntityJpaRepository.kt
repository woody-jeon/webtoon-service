package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.storage.db.core.episode.entity.UserEpisodeAccessEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserEpisodeAccessEntityJpaRepository : JpaRepository<UserEpisodeAccessEntity, Long> {
    fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): UserEpisodeAccessEntity?
}
