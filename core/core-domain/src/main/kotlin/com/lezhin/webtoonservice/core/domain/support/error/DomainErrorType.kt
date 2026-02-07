package com.lezhin.webtoonservice.core.domain.support.error

enum class DomainErrorType(val code: DomainErrorCode, val message: String) {
    USER_NOT_FOUND(DomainErrorCode.E1000, "존재하지 않는 사용자입니다."),
    EPISODE_NOT_FOUND(DomainErrorCode.E2000, "존재하지 않는 회차입니다."),
    EPISODE_NOT_AVAILABLE(DomainErrorCode.E2000, "구매할 수 없는 회차입니다."),
    ALREADY_PURCHASED(DomainErrorCode.E3000, "이미 구매한 회차입니다."),
    INSUFFICIENT_BALANCE(DomainErrorCode.E4000, "잔액이 부족합니다."),
    ACCESS_DENIED(DomainErrorCode.E5000, "구매하지 않은 회차입니다."),
    CONTENT_EXPIRED(DomainErrorCode.E5000, "열람 기간이 만료되었습니다."),
    PURCHASE_FAILED(DomainErrorCode.E6000, "결제 처리 중 오류가 발생했습니다."),
    DEFAULT_ERROR(DomainErrorCode.E9999, "An unexpected error has occurred."),
}
