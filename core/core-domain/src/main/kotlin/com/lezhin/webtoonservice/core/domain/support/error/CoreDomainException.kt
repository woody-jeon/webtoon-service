package com.lezhin.webtoonservice.core.domain.support.error

class CoreDomainException(
    val errorType: DomainErrorType,
) : RuntimeException(errorType.message)
