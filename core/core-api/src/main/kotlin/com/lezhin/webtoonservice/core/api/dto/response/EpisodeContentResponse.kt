package com.lezhin.webtoonservice.core.api.dto.response

import com.fasterxml.jackson.databind.JsonNode
import com.lezhin.webtoonservice.core.api.support.utils.DateTimeUtils
import com.lezhin.webtoonservice.core.domain.episode.model.Episode

data class EpisodeContentResponse(
    val episodeId: Long,
    val title: String,
    val thumbnail: String,
    val content: JsonNode,
    val updatedAt: String,
) {
    companion object {
        fun of(episode: Episode): EpisodeContentResponse =
            EpisodeContentResponse(
                episodeId = episode.id,
                title = episode.title,
                thumbnail = episode.thumbnail,
                content = episode.content,
                updatedAt = DateTimeUtils.format(episode.updatedAt),
            )
    }
}
