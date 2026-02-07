package com.lezhin.webtoonservice.core.api.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ApiErrorType(
    val status: HttpStatus,
    val code: String,
    val message: String,
    val logLevel: LogLevel,
) {
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST, "WEBTOON_400", "Invalid request value.", LogLevel.ERROR),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "WEBTOON_500", "An unexpected error has occurred.", LogLevel.ERROR),
}
