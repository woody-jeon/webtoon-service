package com.lezhin.webtoonservice.core.domain.episode.model

import java.time.LocalDateTime

data class EpisodeContent(
    val episodeId: Long,
    val title: String,
    val contentUrl: String?,
    val images: List<EpisodeImage>,
    val accessedAt: LocalDateTime,
)

data class EpisodeImage(
    val sequence: Int,
    val url: String,
)
