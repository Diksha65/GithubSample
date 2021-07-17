package com.project.githubsample.model

import com.squareup.moshi.Json

data class RepoResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "node_id")
    val nodeId: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "full_name")
    val fullName: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "language")
    val language: String? = null,

    @Json(name = "default_branch")
    val defaultBranch: String? = null,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String,

    @Json(name = "html_url")
    val htmlUrl: String? = null,

    @Json(name = "pulls_url")
    val pullsUrl: String? = null
)