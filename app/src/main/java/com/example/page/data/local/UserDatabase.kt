package com.example.page.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.page.data.local.dao.RemoteKeysDao
import com.example.page.data.local.dao.UserDao
import com.example.page.data.local.model.RemoteKeys
import com.example.page.data.local.model.UserEntity

@Database(
    entities = [UserEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        var USER_DATABASE = "user_database"
    }
}