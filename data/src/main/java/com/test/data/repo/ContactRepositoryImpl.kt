package com.test.data.repo

import com.test.data.model.toUser
import com.test.data.remote.RandomUserApi
import com.test.domain.repo.ContactRepository
import com.test.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val randomUserApi: RandomUserApi
) : ContactRepository {

    override suspend fun fetchContactsPage(page: Int): Result<List<User>> = runCatching {
        withContext(Dispatchers.IO) {
            randomUserApi.fetchContacts(page = page)?.results?.map { it.toUser() }
                ?: error("Contact list cannot be retrieved on this page : $page")
        }
    }
}