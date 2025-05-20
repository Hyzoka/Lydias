package com.test.data.model

import com.test.domain.User

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