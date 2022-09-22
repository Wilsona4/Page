package com.example.page.data.remote

import com.example.page.data.remote.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getUsers(
        @Query("q") query: String = "lagos",
        @Query("page") page: Int,
    ): UserResponse
}