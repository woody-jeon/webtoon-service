package com.lezhin.webtoonservice.core.api.dto.response

import com.lezhin.webtoonservice.core.api.support.utils.DateTimeUtils
import com.lezhin.webtoonservice.core.domain.purchase.model.Purchase

data class PurchaseResponse(
    val purchaseId: Long,
    val userId: Long,
    val episodeId: Long,
    val amount: Int,
    val purchasedAt: String,
    val status: String,
) {
    companion object {
        fun of(purchase: Purchase): PurchaseResponse =
            PurchaseResponse(
                purchaseId = purchase.id,
                userId = purchase.userId,
                episodeId = purchase.episodeId,
                amount = purchase.amount.toInt(),
                purchasedAt = DateTimeUtils.format(purchase.completedAt!!),
                status = purchase.status.name,
            )
    }
}
