package com.example.page.di

import com.example.page.di.MyDispatchers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Dispatcher(DEFAULT)
    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Dispatcher(IO)
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Dispatcher(MAIN)
    @Provides
    fun providesMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Dispatcher(UNCONFINED)
    @Provides
    fun providesUnconfinedDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined

}