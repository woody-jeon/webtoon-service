package com.lezhin.webtoonservice.core.api.support.error

class CustomMethodArgumentNotValidException(
    val errors: List<FieldError>,
) : RuntimeException()
