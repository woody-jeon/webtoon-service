package com.lezhin.webtoonservice.core.api.support.error

data class FieldError private constructor(
    val field: String,
    val message: String,
) {
    companion object {
        fun of(
            field: String,
            message: String,
        ): FieldError = FieldError(field, message)

        fun of(error: org.springframework.validation.FieldError): FieldError =
            FieldError(error.field, error.defaultMessage ?: "Invalid value")
    }
}
