package com.lezhin.webtoonservice.storage.db.core.episode.repository

import com.lezhin.webtoonservice.storage.db.core.episode.entity.EpisodeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EpisodeEntityJpaRepository : JpaRepository<EpisodeEntity, Long>
