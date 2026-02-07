package com.lezhin.webtoonservice.core.domain.episode.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Episode(
    val id: Long,
    val webtoonId: Long,
    val episodeNumber: Int,
    val title: String,
    val price: BigDecimal,
    val contentUrl: String?,
    val isAvailable: Boolean,
    val createdAt: LocalDateTime,
) {
    fun canBePurchased(): Boolean = isAvailable
}
