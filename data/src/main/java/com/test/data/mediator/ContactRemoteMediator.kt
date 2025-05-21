package com.test.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.data.local.AppDatabase
import com.test.data.model.dto.toEntity
import com.test.data.model.entity.RemoteKeys
import com.test.data.model.entity.UserEntity
import com.test.data.remote.RandomUserApi

@OptIn(ExperimentalPagingApi::class)
class ContactRemoteMediator(
    private val db: AppDatabase,
    private val api: RandomUserApi,
    private val isOnline: Boolean
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        if (!isOnline) return MediatorResult.Success(endOfPaginationReached = true)


        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        return try {
            val response = api.fetchContacts(page = page, results = state.config.pageSize)
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getRemoteKeysDao().clearRemoteKeys()
                    db.userDao().clearAll()
                }

                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = response?.results?.let { data ->
                    data.map {
                        RemoteKeys(
                            contactEmail = it.email,
                            prevKey = prevKey,
                            currentPage = page,
                            nextKey = nextKey
                        )
                    }
                }
                db.getRemoteKeysDao().insertAll(remoteKeys.orEmpty())
                db.userDao().insertAll(response?.results?.map { it.toEntity() }.orEmpty())
            }
            MediatorResult.Success(endOfPaginationReached = response?.results?.isEmpty() ?: true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.email?.let { email ->
                db.getRemoteKeysDao().getRemoteKeyByContactEmail(email)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { user ->
            db.getRemoteKeysDao().getRemoteKeyByContactEmail(user.email)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { user ->
            db.getRemoteKeysDao().getRemoteKeyByContactEmail(user.email)
        }
    }
}

