package com.lezhin.webtoonservice.core.domain.support.error

data class DomainErrorMessage private constructor(
    val code: String,
    val message: String,
) {
    constructor(errorType: DomainErrorType) : this(
        code = errorType.code,
        message = errorType.message,
    )
}
