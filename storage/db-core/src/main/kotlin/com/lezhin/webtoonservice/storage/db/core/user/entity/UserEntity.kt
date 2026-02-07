package com.lezhin.webtoonservice.storage.db.core.user.entity

import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.storage.db.core.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import java.math.BigDecimal

@Entity
@Table(name = "users")
@Comment("사용자")
class UserEntity(
    @Column(nullable = false, precision = 12, scale = 2)
    @Comment("잔액")
    var balance: BigDecimal = BigDecimal.ZERO,
) : BaseEntity() {
    fun toDomain(): User =
        User(
            id = id,
            balance = balance,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    fun decrementBalance(amount: BigDecimal) {
        this.balance = this.balance.subtract(amount)
    }

    companion object {
        fun from(user: User): UserEntity =
            UserEntity(
                balance = user.balance,
            )
    }
}
