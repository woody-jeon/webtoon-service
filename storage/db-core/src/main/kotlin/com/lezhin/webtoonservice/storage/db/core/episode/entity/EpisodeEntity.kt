package com.lezhin.webtoonservice.storage.db.core.episode.entity

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
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
    @Column(nullable = false, length = 200)
    @Comment("회차 제목")
    val title: String,
    @Column(nullable = false, precision = 10, scale = 2)
    @Comment("가격")
    val price: BigDecimal,
    @Column(name = "content_url")
    @Comment("콘텐츠 URL")
    val contentUrl: String? = null,
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
            contentUrl = contentUrl,
            isAvailable = isAvailable,
            createdAt = createdAt,
        )

    companion object {
        fun from(episode: Episode): EpisodeEntity =
            EpisodeEntity(
                webtoonId = episode.webtoonId,
                episodeNumber = episode.episodeNumber,
                title = episode.title,
                price = episode.price,
                contentUrl = episode.contentUrl,
                isAvailable = episode.isAvailable,
            )
    }
}
