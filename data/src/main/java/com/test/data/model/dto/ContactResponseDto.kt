package com.test.data.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactResponseDto(
    @Json(name = "results") val results: List<UserDto>,
    val info: PageInfoDto
)