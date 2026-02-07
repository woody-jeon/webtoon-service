package com.lezhin.webtoonservice.core.domain.episode.model

import java.time.LocalDateTime

data class UserEpisodeAccess(
    val id: Long,
    val userId: Long,
    val episodeId: Long,
    val purchaseId: Long,
    val grantedAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
    val lastAccessedAt: LocalDateTime?,
) {
    fun isExpired(): Boolean = expiresAt?.isBefore(LocalDateTime.now()) ?: false
}
