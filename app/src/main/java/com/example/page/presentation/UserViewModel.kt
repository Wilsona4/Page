package com.example.page.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.page.data.local.model.asDomainModel
import com.example.page.domain.model.User
import com.example.page.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    val users: Flow<PagingData<User>> =
        userRepository.getUsers()
            .map { pagingData ->
                pagingData.map { userEntity ->
                    userEntity.asDomainModel()
                }
            }
            .cachedIn(viewModelScope)

    val favouriteUsers: Flow<PagingData<User>> =
        users.map { pagingData ->
            pagingData.filter { user ->
                user.isFavourite
            }
        }.cachedIn(viewModelScope)

    suspend fun updateFavourite(isFavourite: Boolean, user: User) {
        userRepository.updateFavourite(isFavourite, user)
    }
}
