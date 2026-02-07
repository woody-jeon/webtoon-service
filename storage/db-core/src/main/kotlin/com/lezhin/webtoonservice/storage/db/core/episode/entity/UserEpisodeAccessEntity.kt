package com.lezhin.webtoonservice.storage.db.core.episode.entity

import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisodeAccess
import com.lezhin.webtoonservice.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_episode_accesses",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_user_episode_access", columnNames = ["user_id", "episode_id"]),
    ],
    indexes = [Index(name = "idx_user_expires", columnList = "user_id, expires_at")],
)
@Comment("사용자 회차 접근 권한")
class UserEpisodeAccessEntity(
    @Column(name = "user_id", nullable = false)
    @Comment("사용자 ID")
    val userId: Long,
    @Column(name = "episode_id", nullable = false)
    @Comment("회차 ID")
    val episodeId: Long,
    @Column(name = "purchase_id", nullable = false)
    @Comment("구매 ID")
    val purchaseId: Long,
    @Column(name = "granted_at", nullable = false)
    @Comment("권한 부여 일시")
    val grantedAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "expires_at")
    @Comment("만료 일시")
    val expiresAt: LocalDateTime? = null,
    @Column(name = "last_accessed_at")
    @Comment("마지막 접근 일시")
    var lastAccessedAt: LocalDateTime? = null,
) : BaseEntity() {
    fun toDomain(): UserEpisodeAccess =
        UserEpisodeAccess(
            id = id,
            userId = userId,
            episodeId = episodeId,
            purchaseId = purchaseId,
            grantedAt = grantedAt,
            expiresAt = expiresAt,
            lastAccessedAt = lastAccessedAt,
        )

    fun updateLastAccessedAt() {
        this.lastAccessedAt = LocalDateTime.now()
    }

    companion object {
        fun from(access: UserEpisodeAccess): UserEpisodeAccessEntity =
            UserEpisodeAccessEntity(
                userId = access.userId,
                episodeId = access.episodeId,
                purchaseId = access.purchaseId,
                grantedAt = access.grantedAt,
                expiresAt = access.expiresAt,
                lastAccessedAt = access.lastAccessedAt,
            )
    }
}
