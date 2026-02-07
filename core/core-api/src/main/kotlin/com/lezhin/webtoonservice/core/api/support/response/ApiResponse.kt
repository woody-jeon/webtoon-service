package com.lezhin.webtoonservice.core.api.support.response

import com.lezhin.webtoonservice.core.api.support.error.ApiErrorMessage
import com.lezhin.webtoonservice.core.api.support.error.ApiErrorType
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorMessage
import com.lezhin.webtoonservice.core.domain.support.error.DomainErrorType

data class ApiResponse<T> private constructor(
    val result: ResultType,
    val data: T? = null,
    val error: Any? = null,
) {
    companion object {
        fun success(): ApiResponse<Any> = ApiResponse(ResultType.SUCCESS, null, null)

        fun <S> success(data: S): ApiResponse<S> = ApiResponse(ResultType.SUCCESS, data, null)

        fun <S> error(
            error: ApiErrorType,
            errorData: Any? = null,
        ): ApiResponse<S> = ApiResponse(ResultType.ERROR, null, ApiErrorMessage(error, errorData))

        fun error(error: DomainErrorType): ApiResponse<Any> = ApiResponse(ResultType.ERROR, null, DomainErrorMessage(error))
    }
}
