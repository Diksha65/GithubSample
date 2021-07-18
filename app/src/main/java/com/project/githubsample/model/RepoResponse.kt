package com.project.githubsample.model

import com.google.gson.annotations.SerializedName

data class RepositoryItem(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("language")
    val language: String? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("html_url")
    val htmlUrl: String
)