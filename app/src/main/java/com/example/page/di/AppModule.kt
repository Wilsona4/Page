package com.example.page.di

import android.content.Context
import androidx.room.Room
import com.example.page.data.local.UserDatabase
import com.example.page.data.local.UserDatabase.Companion.USER_DATABASE
import com.example.page.data.local.dao.RemoteKeysDao
import com.example.page.data.local.dao.UserDao
import com.example.page.data.remote.ApiService
import com.example.page.data.repository.UserRepositoryImpl
import com.example.page.domain.repository.IUserRepository
import com.example.page.util.Constants.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                response
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesUserDataBase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, USER_DATABASE)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesUserDAO(database: UserDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun providesRemoteKeysDAO(database: UserDatabase): RemoteKeysDao = database.remoteKeysDao()
}