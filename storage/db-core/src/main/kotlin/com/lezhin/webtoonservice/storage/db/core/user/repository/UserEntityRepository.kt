package com.lezhin.webtoonservice.storage.db.core.user.repository

import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.core.domain.user.model.repository.UserRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class UserEntityRepository(
    private val userEntityJpaRepository: UserEntityJpaRepository,
) : UserRepository {
    override fun findById(id: Long): User? {
        return userEntityJpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByIdWithLock(id: Long): User? {
        return userEntityJpaRepository.findWithLockById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun decrementBalanceIfSufficient(
        userId: Long,
        amount: BigDecimal,
    ): Boolean {
        val userEntity =
            userEntityJpaRepository.findWithLockById(userId).orElse(null)
                ?: return false

        if (userEntity.balance < amount) {
            return false
        }

        userEntity.decrementBalance(amount)
        return true
    }
}
