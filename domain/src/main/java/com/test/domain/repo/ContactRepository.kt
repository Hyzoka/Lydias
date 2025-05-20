package com.test.domain.repo

import com.test.domain.User

interface ContactRepository {
    suspend fun fetchContactsPage(page: Int): Result<List<User>>

}