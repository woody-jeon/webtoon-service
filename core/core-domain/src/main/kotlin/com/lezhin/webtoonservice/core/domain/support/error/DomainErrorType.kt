package com.lezhin.webtoonservice.core.domain.support.error

enum class DomainErrorType(
    val status: Int,
    val code: String,
    val message: String,
) {
    WEBTOON_EPISODE_NOT_AVAILABLE(200, "WEBTOON_1002", "구매할 수 없는 회차입니다."),
    WEBTOON_ALREADY_PURCHASED(200, "WEBTOON_1003", "이미 구매한 회차입니다."),
    WEBTOON_INSUFFICIENT_COIN(200, "WEBTOON_1004", "코인이 부족합니다."),
    WEBTOON_ACCESS_DENIED(200, "WEBTOON_1005", "구매하지 않은 회차입니다."),
    WEBTOON_CONTENT_EXPIRED(200, "WEBTOON_1006", "열람 기간이 만료되었습니다."),
    WEBTOON_IDEMPOTENCY_KEY_MISMATCH(200, "WEBTOON_1008", "멱등성 키가 다른 구매 요청에 이미 사용되었습니다."),

    DEFAULT_ERROR(500, "WEBTOON_500", "An unexpected error has occurred."),
    USER_NOT_FOUND(500, "WEBTOON_5001", "존재하지 않는 사용자입니다."),
    EPISODE_NOT_FOUND(500, "WEBTOON_5002", "존재하지 않는 회차입니다."),
    PURCHASE_FAILED(500, "WEBTOON_5003", "결제 처리 중 오류가 발생했습니다."),
}
