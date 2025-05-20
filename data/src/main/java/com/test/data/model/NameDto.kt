package com.test.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NameDto(
    val title: String,
    val first: String,
    val last: String
)
