package com.example.page.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.page.data.local.UserDatabase
import com.example.page.data.local.model.UserEntity
import com.example.page.data.remote.ApiService
import com.example.page.domain.model.User
import com.example.page.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase
) : IUserRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getUsers(): Flow<PagingData<UserEntity>> = Pager(
        config = PagingConfig(NETWORK_PAGE_SIZE),
        remoteMediator = UserRemoteMediator(apiService, userDatabase)
    ) {
        userDatabase.userDao().readUsers()
    }.flow

    override suspend fun updateFavourite(isFavourite: Boolean, user: User) {
        userDatabase.userDao().updateUser(isFavourite, user.id)
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}

