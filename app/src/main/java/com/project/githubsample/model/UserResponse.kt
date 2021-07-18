package com.project.githubsample.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class UserResponse(
    @Json(name = "login")
    val login: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "company")
    val company: String? = null,

    @Json(name = "public_repos")
    val publicRepos: Int,

    @Json(name = "followers")
    val followers: Int,

    @Json(name = "following")
    val following: Int
)