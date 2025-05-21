package com.test.data.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DobDto(val date: String, val age: Int)
