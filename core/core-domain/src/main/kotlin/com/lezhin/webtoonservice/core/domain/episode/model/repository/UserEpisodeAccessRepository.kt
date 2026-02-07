package com.lezhin.webtoonservice.core.domain.episode.model.repository

import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisodeAccess

interface UserEpisodeAccessRepository {
    fun findByUserIdAndEpisodeId(
        userId: Long,
        episodeId: Long,
    ): UserEpisodeAccess?

    fun save(
        userId: Long,
        episodeId: Long,
        purchaseId: Long,
    )

    fun updateLastAccessedAt(
        userId: Long,
        episodeId: Long,
    )
}
