package com.test.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.test.data.local.AppDatabase
import com.test.data.mediator.ContactRemoteMediator
import com.test.data.model.entity.toUser
import com.test.data.remote.RandomUserApi
import com.test.domain.NetworkConnectivityHelper
import com.test.domain.model.User
import com.test.domain.repo.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val api: RandomUserApi,
    private val networkMonitor: NetworkConnectivityHelper
) : ContactRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getPaginatedContacts(): Flow<PagingData<User>> {
        val pagingSourceFactory = { db.userDao().getUsersPaging() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = ContactRemoteMediator(db, api, networkMonitor.isConnected.value),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toUser() }
        }
    }

    override fun getUserByEmail(email: String): Flow<User> {
        return db.userDao().getUserByEmail(email).map { it.toUser() }
    }


}