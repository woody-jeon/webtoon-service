package com.lezhin.webtoonservice.storage.db.core.purchase.entity

import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase
import com.lezhin.webtoonservice.core.enums.PurchaseStatus
import com.lezhin.webtoonservice.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.Comment
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "purchases",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_user_episode", columnNames = ["user_id", "episode_id"]),
        UniqueConstraint(name = "uk_idempotency", columnNames = ["idempotency_key"]),
    ],
    indexes = [
        Index(name = "idx_user_created", columnList = "user_id, created_at"),
        Index(name = "idx_idempotency_expires", columnList = "idempotency_key, idempotency_expires_at"),
    ],
)
@Comment("구매 내역")
class PurchaseEntity(
    @Column(name = "user_id", nullable = false)
    @Comment("사용자 ID")
    val userId: Long,
    @Column(name = "episode_id", nullable = false)
    @Comment("회차 ID")
    val episodeId: Long,
    @Column(name = "idempotency_key", nullable = false, length = 100)
    @Comment("멱등성 키")
    val idempotencyKey: String,
    @Column(name = "idempotency_expires_at", nullable = false)
    @Comment("멱등성 키 만료 시간")
    val idempotencyExpiresAt: LocalDateTime,
    @Column(nullable = false, precision = 10, scale = 2)
    @Comment("결제 금액")
    val amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Comment("결제 상태")
    val status: PurchaseStatus = PurchaseStatus.PENDING,
    @Column(name = "completed_at")
    @Comment("완료 일시")
    val completedAt: LocalDateTime? = null,
) : BaseEntity() {
    fun toDomain(): Purchase =
        Purchase(
            id = id,
            userId = userId,
            episodeId = episodeId,
            idempotencyKey = idempotencyKey,
            idempotencyExpiresAt = idempotencyExpiresAt,
            amount = amount,
            status = status,
            createdAt = createdAt,
            completedAt = completedAt,
        )

    companion object {
        fun from(purchase: Purchase): PurchaseEntity =
            PurchaseEntity(
                userId = purchase.userId,
                episodeId = purchase.episodeId,
                idempotencyKey = purchase.idempotencyKey,
                idempotencyExpiresAt = purchase.idempotencyExpiresAt,
                amount = purchase.amount,
                status = purchase.status,
                completedAt = purchase.completedAt,
            )
    }
}
