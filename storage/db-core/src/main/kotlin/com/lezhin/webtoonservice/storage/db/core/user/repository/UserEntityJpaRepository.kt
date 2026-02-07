package com.lezhin.webtoonservice.storage.db.core.user.repository

import com.lezhin.webtoonservice.storage.db.core.user.entity.UserEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal

interface UserEntityJpaRepository : JpaRepository<UserEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(id: Long): UserEntity?

    @Modifying
    @Query("UPDATE UserEntity u SET u.coin = u.coin - :amount WHERE u.id = :userId")
    fun decrementCoin(
        @Param("userId") userId: Long,
        @Param("amount") amount: BigDecimal,
    )
}
