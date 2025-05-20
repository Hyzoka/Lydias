package com.test.domain

interface ContactRepository {

    suspend fun getUsers(page: Int): Result<List<User>>

}