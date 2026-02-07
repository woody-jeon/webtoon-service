package com.lezhin.webtoonservice.core.api.support.error

data class ApiErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorType: ApiErrorType, data: Any? = null) : this(
        code = errorType.code.name,
        message = errorType.message,
        data = data,
    )
}
