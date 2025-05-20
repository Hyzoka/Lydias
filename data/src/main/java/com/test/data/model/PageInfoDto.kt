package com.test.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PageInfoDto(
    val page: Int,
    val results: Int,
    val seed: String
)