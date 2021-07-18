package com.project.githubsample.model

import com.squareup.moshi.Json

data class RepositoryItem(
    @Json(name = "name")
    val name: String,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "language")
    val language: String? = null,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "updated_at")
    val updatedAt: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "pulls_url")
    val pullsUrl: String
)