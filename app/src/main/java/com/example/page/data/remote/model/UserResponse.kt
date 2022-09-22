package com.example.page.data.remote.model

data class UserResponse(
    val incomplete_results: Boolean,
    val items: List<UserDTO>,
    val total_count: Int
)