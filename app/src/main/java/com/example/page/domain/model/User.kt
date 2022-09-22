package com.example.page.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val avatar_url: String,
    val gists_url: String,
    val id: Int,
    val login: String,
    val repos_url: String,
    val organizations_url: String,
    val type: String,
    val html_url: String,
    var isFavourite: Boolean
) : Parcelable