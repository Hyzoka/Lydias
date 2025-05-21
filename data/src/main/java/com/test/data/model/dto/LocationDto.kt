package com.test.data.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDto(val city: String, val country: String)