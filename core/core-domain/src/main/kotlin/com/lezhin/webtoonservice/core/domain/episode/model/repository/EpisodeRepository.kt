package com.lezhin.webtoonservice.core.domain.episode.model.repository

import com.lezhin.webtoonservice.core.domain.episode.model.Episode

interface EpisodeRepository {
    fun findById(id: Long): Episode?
}
