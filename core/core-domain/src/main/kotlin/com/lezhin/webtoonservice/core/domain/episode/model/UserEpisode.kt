package com.lezhin.webtoonservice.core.domain.episode.model

import com.lezhin.webtoonservice.core.domain.user.model.UserId

data class UserEpisode(
    val userId: UserId,
    val episodeId: EpisodeId,
)
