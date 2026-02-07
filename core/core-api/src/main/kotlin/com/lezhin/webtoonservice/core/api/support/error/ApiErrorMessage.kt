package com.lezhin.webtoonservice.core.api.support.error

data class ApiErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorType: ApiErrorType, data: Any? = null) : this(
        code = errorType.code,
        message = errorType.message,
        data = data,
    )

    constructor(code: String, message: String) : this(
        code = code,
        message = message,
        data = null,
    )
}
