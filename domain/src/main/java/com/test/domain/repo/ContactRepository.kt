package com.test.domain.repo

import androidx.paging.PagingData
import com.test.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    fun getPaginatedContacts(): Flow<PagingData<User>>

}