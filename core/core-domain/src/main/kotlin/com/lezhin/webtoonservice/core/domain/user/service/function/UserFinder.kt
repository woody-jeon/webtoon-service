package com.lezhin.webtoonservice.core.domain.user.service.function

import com.lezhin.webtoonservice.core.domain.support.error.CoreDomainException
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorType
import com.lezhin.webtoonservice.core.domain.user.model.User
import com.lezhin.webtoonservice.core.domain.user.model.UserId
import com.lezhin.webtoonservice.core.domain.user.model.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userRepository: UserRepository,
) {
    fun findById(userId: UserId): User =
        userRepository.findById(userId.id)
            ?: throw CoreDomainException(DomainErrorType.USER_NOT_FOUND)

    fun findByIdWithLock(userId: UserId): User =
        userRepository.findByIdWithLock(userId.id)
            ?: throw CoreDomainException(DomainErrorType.USER_NOT_FOUND)
}
