package com.test.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.test.domain.model.User

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "email") val email: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "cell") val cell: String,
    @Json(name = "picture") val picture: PictureDto,
    @Json(name = "name") val name: NameDto,
    @Json(name = "location") val location: LocationDto,
    @Json(name = "dob") val dob: DobDto,
    @Json(name = "nat") val nationality: String,
    @Json(name = "login") val login: LoginDto
)

fun UserDto.toUser() = User(
    uuid = login.uuid,
    fullName = "${name.first} ${name.last}",
    email = email,
    phone = phone,
    cell = cell,
    pictureUrl = picture.large,
    dob = dob.date,
    age = dob.age,
    location = "${location.city}, ${location.country}",
    nationality = nationality
)
