package com.example.page.domain.repository

import androidx.paging.PagingData
import com.example.page.data.local.model.UserEntity
import com.example.page.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUsers(): Flow<PagingData<UserEntity>>
    suspend fun updateFavourite(isFavourite: Boolean, user: User)
}