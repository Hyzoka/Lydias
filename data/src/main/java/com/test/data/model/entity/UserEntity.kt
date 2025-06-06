package com.test.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String, // Utilisé comme ID unique
    val phone: String,
    val cell: String,
    val pictureUrl: String,
    val fullName: String,
    val city: String,
    val country: String,
    val birthDate: String,
    val age: Int,
    val nationality: String,
)

fun UserEntity.toUser(): User {
    return User(
        fullName = fullName,
        email = email,
        phone = phone,
        cell = cell,
        pictureUrl = pictureUrl,
        birthday = birthDate,
        age = age,
        location = "$city, $country",
        nationality = nationality
    )
}

