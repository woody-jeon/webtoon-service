package com.lezhin.webtoonservice.core.api.support.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    const val YYYY_MM_DD_HH_MM: String = "yyyy-MM-dd HH:mm"
    val dateFormat: String = "yyyy.MM.dd"

    // String -> LocalDateTime (커스텀 포맷 사용 가능)
    fun parse(
        dateTimeString: String,
        pattern: String = "yyyy-MM-dd HH:mm:ss",
    ): LocalDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern))

    // LocalDateTime -> String (커스텀 포맷 사용 가능)
    fun format(
        localDateTime: LocalDateTime,
        pattern: String = "yyyy-MM-dd HH:mm:ss",
    ): String = localDateTime.format(DateTimeFormatter.ofPattern(pattern))
}
