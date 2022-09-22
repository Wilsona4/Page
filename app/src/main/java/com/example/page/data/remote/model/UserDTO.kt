package com.example.page.data.remote.model

import com.example.page.data.local.model.UserEntity

data class UserDTO(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
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
    val url: String
)

fun UserDTO.asEntity() = UserEntity(
    avatar_url = avatar_url,
    events_url = events_url,
    followers_url = followers_url,
    following_url = following_url,
    gists_url = gists_url,
    gravatar_id = gravatar_id,
    html_url = html_url,
    id = id,
    login = login,
    node_id = node_id,
    organizations_url = organizations_url,
    received_events_url = received_events_url,
    repos_url = repos_url,
    score = score,
    site_admin = site_admin,
    starred_url = starred_url,
    subscriptions_url = subscriptions_url,
    type = type,
    url = url,
    favourite = false
)