package com.example.page.di

import com.example.page.data.repository.UserRepositoryImpl
import com.example.page.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(
        userRepository: UserRepositoryImpl
    ): IUserRepository
}