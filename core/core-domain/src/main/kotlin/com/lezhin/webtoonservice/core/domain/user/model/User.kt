package com.lezhin.webtoonservice.core.domain.user.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class User(
    val id: Long,
    val coin: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun hasEnoughCoin(amount: BigDecimal): Boolean = coin >= amount
}
