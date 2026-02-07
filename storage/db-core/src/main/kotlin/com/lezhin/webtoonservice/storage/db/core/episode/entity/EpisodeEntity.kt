package com.lezhin.webtoonservice.storage.db.core.episode.entity

import com.fasterxml.jackson.databind.JsonNode
import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal

@Entity
@Table(
    name = "episodes",
    indexes = [Index(name = "idx_webtoon_episode", columnList = "webtoon_id, episode_number")],
)
@Comment("웹툰 회차")
class EpisodeEntity(
    @Column(name = "webtoon_id", nullable = false)
    @Comment("웹툰 ID")
    val webtoonId: Long,
    @Column(name = "episode_number", nullable = false)
    @Comment("회차 번호")
    val episodeNumber: Int,
    @Column(name = "title", nullable = false, length = 200)
    @Comment("회차 제목")
    val title: String,
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Comment("가격")
    val price: BigDecimal,
    @Column(name = "thumbnail", nullable = false, length = 500)
    @Comment("썸네일")
    val thumbnail: String,
    @Column(name = "content")
    @JdbcTypeCode(SqlTypes.JSON)
    @Comment("콘텐츠")
    val content: JsonNode,
    @Column(name = "is_available", nullable = false)
    @Comment("구매 가능 여부")
    val isAvailable: Boolean = true,
) : BaseEntity() {
    fun toDomain(): Episode =
        Episode(
            id = id,
            webtoonId = webtoonId,
            episodeNumber = episodeNumber,
            title = title,
            price = price,
            thumbnail = thumbnail,
            content = content,
            isAvailable = isAvailable,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
