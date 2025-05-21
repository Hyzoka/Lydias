package com.test.data.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PictureDto(
    val thumbnail: String,
    val medium: String,
    val large: String
)