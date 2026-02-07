package com.lezhin.webtoonservice.core.api.controller

import com.lezhin.webtoonservice.core.api.support.response.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/health")
class HealthCheckController {
    @GetMapping
    fun health(): ApiResponse<Any> {
        println("Hello World!")
        return ApiResponse.success()
    }
}
