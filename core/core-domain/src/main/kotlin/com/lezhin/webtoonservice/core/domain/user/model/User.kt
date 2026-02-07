package com.lezhin.webtoonservice.core.domain.user.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class User(
    val id: Long,
    val balance: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun hasEnoughBalance(amount: BigDecimal): Boolean = balance >= amount
}
