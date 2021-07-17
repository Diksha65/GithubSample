package com.project.githubsample.model

import com.squareup.moshi.Json
import java.sql.Timestamp

data class RepoResponse(
    @Json(name = "data")
    val data: List<RepoItem>
)

data class RepoItem(
    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "language")
    val language: String? = null,

    @Json(name = "created_at")
    val createdAt: Timestamp,

    @Json(name = "updated_at")
    val updatedAt: Timestamp,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "pulls_url")
    val pullsUrl: String
)