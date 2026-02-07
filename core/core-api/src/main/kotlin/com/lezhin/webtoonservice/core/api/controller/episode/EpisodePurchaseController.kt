package com.lezhin.webtoonservice.core.api.controller.episode

import com.lezhin.webtoonservice.core.api.dto.response.PurchaseResponse
import com.lezhin.webtoonservice.core.api.support.response.ApiResponse
import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisode
import com.lezhin.webtoonservice.core.domain.purchase.model.IdempotencyKey
import com.lezhin.webtoonservice.core.domain.purchase.service.EpisodePurchaseService
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users/{userId}/episodes/{episodeId}")
class EpisodePurchaseController(
    private val episodePurchaseService: EpisodePurchaseService,
) {
    @PostMapping("/purchase")
    fun purchaseEpisode(
        @PathVariable userId: Long,
        @PathVariable episodeId: Long,
        @RequestHeader("Idempotency-Key") idempotencyKey: String,
    ): ApiResponse<PurchaseResponse> {
        val purchase =
            episodePurchaseService.purchaseEpisode(
                userEpisode = UserEpisode(UserId(userId), EpisodeId(episodeId)),
                idempotencyKey = IdempotencyKey(idempotencyKey),
            )
        val response = PurchaseResponse.of(purchase)
        return ApiResponse.success(response)
    }
}
