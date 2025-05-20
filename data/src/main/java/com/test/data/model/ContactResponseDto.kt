package com.test.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContactResponseDto(
    val results: List<UserDto>,
    val info: PageInfoDto
)