package com.test.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.data.model.toUser
import com.test.data.remote.RandomUserApi
import com.test.domain.model.User
import javax.inject.Inject

class ContactPagingSource @Inject constructor(
    private val api: RandomUserApi
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val response = this.api.fetchContacts(page = page)
            val data = response?.results?.map { userDto -> userDto.toUser() }.orEmpty()

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition
    }
}
