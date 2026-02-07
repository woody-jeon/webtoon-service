package com.lezhin.webtoonservice.core.api.support.error

import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus

enum class ApiErrorType(val status: HttpStatus, val code: ApiErrorCode, val message: String, val logLevel: LogLevel) {
    INVALID_REQUEST_VALUE(HttpStatus.BAD_REQUEST, ApiErrorCode.E400, "Invalid request value.", LogLevel.ERROR),
    MISSING_IDEMPOTENCY_KEY(HttpStatus.BAD_REQUEST, ApiErrorCode.E400, "Idempotency-Key 헤더가 필요합니다.", LogLevel.WARN),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, ApiErrorCode.E404, "존재하지 않는 사용자입니다.", LogLevel.WARN),
    EPISODE_NOT_FOUND(HttpStatus.NOT_FOUND, ApiErrorCode.E404, "존재하지 않는 회차입니다.", LogLevel.WARN),
    ALREADY_PURCHASED(HttpStatus.CONFLICT, ApiErrorCode.E409, "이미 구매한 회차입니다.", LogLevel.INFO),
    INSUFFICIENT_BALANCE(HttpStatus.PAYMENT_REQUIRED, ApiErrorCode.E402, "잔액이 부족합니다.", LogLevel.WARN),
    EPISODE_NOT_AVAILABLE(HttpStatus.UNPROCESSABLE_ENTITY, ApiErrorCode.E422, "구매할 수 없는 회차입니다.", LogLevel.WARN),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, ApiErrorCode.E403, "구매하지 않은 회차입니다.", LogLevel.WARN),
    CONTENT_EXPIRED(HttpStatus.GONE, ApiErrorCode.E410, "열람 기간이 만료되었습니다.", LogLevel.WARN),
    PURCHASE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ApiErrorCode.E500, "결제 처리 중 오류가 발생했습니다.", LogLevel.ERROR),
    DEFAULT_ERROR(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ApiErrorCode.E500,
        "An unexpected error has occurred.",
        LogLevel.ERROR,
    ),
}
