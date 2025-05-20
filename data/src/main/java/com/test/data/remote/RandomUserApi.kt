package com.test.data.remote

import com.test.data.model.ContactResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {

    @GET("api/1.3/")
    suspend fun fetchContacts(
        @Query("seed") seed: String = "lydia",
        @Query("results") results: Int = 20,
        @Query("page") page: Int
    ): ContactResponseDto?
}
