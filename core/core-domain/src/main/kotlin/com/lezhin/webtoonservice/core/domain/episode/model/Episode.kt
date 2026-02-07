package com.lezhin.webtoonservice.core.domain.episode.model

import com.fasterxml.jackson.databind.JsonNode
import java.math.BigDecimal
import java.time.LocalDateTime

data class Episode(
    val id: Long,
    val webtoonId: Long,
    val episodeNumber: Int,
    val title: String,
    val price: BigDecimal,
    val thumbnail: String,
    val content: JsonNode,
    val isAvailable: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun canBePurchased(): Boolean = isAvailable
}
