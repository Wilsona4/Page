package com.example.page.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.page.domain.model.User

@Entity(tableName = "user_table")
data class UserEntity(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val score: Double,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String,
    val favourite: Boolean = false
)

fun UserEntity.asDomainModel() = User(
    avatar_url = avatar_url,
    gists_url = gists_url,
    id = id,
    login = login,
    repos_url = repos_url,
    organizations_url = organizations_url,
    type = type,
    html_url = html_url,
    isFavourite = favourite
)


