package com.project.githubsample.model

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "node_id")
    val nodeId: String,

    @Json(name = "login")
    val login: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "repos_url")
    val reposUrl: String,

    @Json(name = "company")
    val company: String,

    @Json(name = "public_repos")
    val publicRepos: Int,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String
)