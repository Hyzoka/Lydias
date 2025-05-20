package com.test.data.model

data class ContactResponseDto(
    val results: List<UserDto>,
    val info: PageInfoDto
)