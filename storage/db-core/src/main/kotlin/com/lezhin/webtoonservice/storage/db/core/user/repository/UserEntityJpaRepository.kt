package com.lezhin.webtoonservice.storage.db.core.user.repository

import com.lezhin.webtoonservice.storage.db.core.user.entity.UserEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.Optional

interface UserEntityJpaRepository : JpaRepository<UserEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(id: Long): Optional<UserEntity>
}
