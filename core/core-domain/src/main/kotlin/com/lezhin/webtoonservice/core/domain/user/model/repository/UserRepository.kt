package com.lezhin.webtoonservice.core.domain.user.model.repository

import com.lezhin.webtoonservice.core.domain.user.model.User
import java.math.BigDecimal

interface UserRepository {
    fun findById(id: Long): User?

    fun findByIdWithLock(id: Long): User?

    fun decrementBalanceIfSufficient(
        userId: Long,
        amount: BigDecimal,
    ): Boolean
}
