package com.lezhin.webtoonservice.core.api.support.error

class CoreApiException(
    val errorType: ApiErrorType,
    val data: Any? = null,
) : RuntimeException(errorType.message)
