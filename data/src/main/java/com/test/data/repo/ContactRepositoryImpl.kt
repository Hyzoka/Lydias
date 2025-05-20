package com.test.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.data.ContactPagingSource
import com.test.data.model.toUser
import com.test.data.remote.RandomUserApi
import com.test.domain.model.User
import com.test.domain.repo.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val randomUserApi: RandomUserApi
) : ContactRepository {

    // create dao
    override fun getPaginatedContacts(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { ContactPagingSource(randomUserApi) }
        ).flow
    }

}