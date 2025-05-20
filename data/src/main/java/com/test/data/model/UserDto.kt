package com.test.data.model

import com.squareup.moshi.JsonClass
import com.test.domain.User

@JsonClass(generateAdapter = true)
data class UserDto(
    val name: NameDto,
    val email: String,
    val phone: String,
    val picture: PictureDto
)

fun UserDto.toUser(): User {
    val fullName = "${name.first} ${name.last}"

    return User(
        fullName = fullName,
        email = email,
        phone = phone,
        pictureUrl = picture.medium
    )
}