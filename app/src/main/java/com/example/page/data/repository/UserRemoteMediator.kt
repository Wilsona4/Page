package com.example.page.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.page.data.local.UserDatabase
import com.example.page.data.local.model.RemoteKeys
import com.example.page.data.local.model.UserEntity
import com.example.page.data.remote.ApiService
import com.example.page.data.remote.model.asEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val apiService: ApiService,
    private val userDatabase: UserDatabase
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val pageKey = when (loadType) {
                LoadType.REFRESH -> GITHUB_STARTING_PAGE_INDEX
//                {
//                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
//                    remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
//                }
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
//                {
//                    val remoteKeys = getRemoteKeyForFirstItem(state)
//                    // If remoteKeys is null, that means the refresh result is not in the database yet.
//                    // We can return Success with `endOfPaginationReached = false` because Paging
//                    // will call this method again if RemoteKeys becomes non-null.
//
//                    // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
//                    // the end of pagination for prepend.
//                    val prevKey = remoteKeys?.prevKey
//                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                    prevKey
//                }

                LoadType.APPEND -> {

                    val remoteKeys = getRemoteKeyForLastItem(state)

                    // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                    // the end of pagination for append.
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                    nextKey
                }
            }

            val apiResponse = apiService.getUsers(page = pageKey)

            val users = apiResponse.items
            val endOfPaginationReached = users.isEmpty()
            userDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    userDatabase.remoteKeysDao().clearRemoteKeys()
                    userDatabase.userDao().clearUsers()
                }
                val prevKey = if (pageKey == GITHUB_STARTING_PAGE_INDEX) null else pageKey.minus(1)
                val nextKey = if (endOfPaginationReached) null else pageKey.plus(1)
                val keys = users.map {
                    RemoteKeys(userId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                userDatabase.remoteKeysDao().insertAll(keys)
                userDatabase.userDao().insertAllUsers(users.map { it.asEntity() })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserEntity>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                // Get the remote keys of the last item retrieved
                userDatabase.remoteKeysDao().remoteKeysRepoId(user.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserEntity>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { user ->
                // Get the remote keys of the first items retrieved
                userDatabase.remoteKeysDao().remoteKeysRepoId(user.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UserEntity>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                userDatabase.remoteKeysDao().remoteKeysRepoId(userId)
            }
        }
    }

    companion object {
        private const val GITHUB_STARTING_PAGE_INDEX = 1
    }
}