package com.test.domain.model


data class User(
    val uuid: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val cell: String,
    val pictureUrl: String,
    val dob: String,
    val age: Int,
    val location: String,
    val nationality: String
)


