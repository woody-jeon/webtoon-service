package com.lezhin.webtoonservice.core.domain.episode.service

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisode
import com.lezhin.webtoonservice.core.domain.episode.service.function.EpisodeFinder
import com.lezhin.webtoonservice.core.domain.episode.service.function.UserEpisodeAccessModifier
import com.lezhin.webtoonservice.core.domain.episode.service.function.UserEpisodeAccessValidator
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.domain.user.service.function.UserFinder
import org.springframework.stereotype.Service

@Service
class EpisodeContentService(
    private val userFinder: UserFinder,
    private val episodeFinder: EpisodeFinder,
    private val userEpisodeAccessValidator: UserEpisodeAccessValidator,
    private val userEpisodeAccessModifier: UserEpisodeAccessModifier,
) {
    fun getEpisodeContent(userEpisode: UserEpisode): Episode {
        userEpisodeAccessValidator.validateAndGetAccess(userEpisode.userId, userEpisode.episodeId)
        val findUser = userFinder.findById(userEpisode.userId)
        val findEpisode = episodeFinder.findById(userEpisode.episodeId)
        val userId = UserId(findUser.id)
        val episodeId = EpisodeId(findEpisode.id)
        userEpisodeAccessModifier.modifyLastAccessedAt(userId, episodeId)

        return findEpisode
    }
}
