package com.lezhin.webtoonservice.core.domain.episode.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.EpisodeId
import com.lezhin.webtoonservice.core.domain.episode.model.UserEpisodeAccess
import com.lezhin.webtoonservice.core.domain.episode.model.repository.UserEpisodeAccessRepository
import com.lezhin.webtoonservice.core.domain.support.error.CoreDomainException
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorType
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import org.springframework.stereotype.Component

@Component
class UserEpisodeAccessValidator(
    private val userEpisodeAccessRepository: UserEpisodeAccessRepository,
) {
    fun validateAndGetAccess(
        userId: UserId,
        episodeId: EpisodeId,
    ): UserEpisodeAccess {
        val access =
            userEpisodeAccessRepository.findByUserIdAndEpisodeId(userId.id, episodeId.id)
                ?: throw CoreDomainException(DomainErrorType.WEBTOON_ACCESS_DENIED)

        if (access.isExpired()) {
            throw CoreDomainException(DomainErrorType.WEBTOON_CONTENT_EXPIRED)
        }

        return access
    }
}
