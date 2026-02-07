package com.lezhin.webtoonservice.storage.db.core.user.repository

import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.core.domain.user.model.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Repository
@Transactional
class UserEntityRepository(
    private val jpaRepository: UserEntityJpaRepository,
    private val queryRepository: UserEntityQueryRepository,
) : UserRepository {
    @Transactional(readOnly = true)
    override fun findById(id: Long): User? = jpaRepository.findByIdOrNull(id)?.toDomain()

    @Transactional(readOnly = true)
    override fun findByIdWithLock(id: Long): User? = jpaRepository.findWithLockById(id)?.toDomain()

    override fun decrementCoin(
        userId: Long,
        amount: BigDecimal,
    ) {
        jpaRepository.decrementCoin(userId, amount)
    }
}
