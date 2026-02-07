package com.lezhin.webtoonservice.core.domain.user.service.function

import com.lezhin.webtoonservice.core.domain.episode.model.Episode
import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.core.domain.user.model.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserModifier(
    private val userRepository: UserRepository,
) {
    fun decrementCoin(
        user: User,
        episode: Episode,
    ) {
        userRepository.decrementCoin(user.id, episode.price)
    }
}
