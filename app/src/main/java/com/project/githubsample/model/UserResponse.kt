package com.project.githubsample.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login")
    val login: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("company")
    val company: String? = null,

    @SerializedName("public_repos")
    val publicRepos: Int,

    @SerializedName("followers")
    val followers: Int,

    @SerializedName("following")
    val following: Int
)