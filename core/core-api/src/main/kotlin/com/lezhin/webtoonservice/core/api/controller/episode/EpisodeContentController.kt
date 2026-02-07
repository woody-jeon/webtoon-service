package com.lezhin.webtoonservice.core.api.controller.episode

import com.lezhin.webtoonservice.core.api.dto.response.EpisodeContentResponse
import com.lezhin.webtoonservice.core.api.support.response.ApiResponse
import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisode
import com.lezhin.webtoonservice.core.domain.episode.service.EpisodeContentService
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users/{userId}/episodes/{episodeId}")
class EpisodeContentController(
    private val episodeContentService: EpisodeContentService,
) {
    @GetMapping("/content")
    fun getEpisodeContent(
        @PathVariable userId: Long,
        @PathVariable episodeId: Long,
    ): ApiResponse<EpisodeContentResponse> {
        val content =
            episodeContentService.getEpisodeContent(
                userEpisode = UserEpisode(UserId(userId), EpisodeId(episodeId)),
            )
        val response = EpisodeContentResponse.of(content)
        return ApiResponse.success(response)
    }
}
