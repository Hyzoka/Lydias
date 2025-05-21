package com.test.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginDto(val uuid: String)