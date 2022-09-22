package com.example.page.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.page.data.local.model.UserEntity

@Dao
interface UserDao {

    /*Add User to Database*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllUsers(user: List<UserEntity>)

    @Query("UPDATE user_table SET favourite=:isFavourite WHERE id=:userId")
    suspend fun updateUser(isFavourite: Boolean, userId: Int)

    /*Get Users in the Database*/
    @Transaction
    @Query("SELECT * FROM user_table")
    fun readUsers(): PagingSource<Int, UserEntity>

    /*Delete User in the Database*/
    @Query("DELETE FROM user_table WHERE favourite = '0'")
    suspend fun clearUsers()
}