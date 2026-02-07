package com.lezhin.webtoonservice.core.domain.episode.model

import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDateTime

data class EpisodeContent(
    val episodeId: EpisodeId,
    val title: String,
    val thumbnail: String,
    val content: JsonNode,
    val accessedAt: LocalDateTime,
)
