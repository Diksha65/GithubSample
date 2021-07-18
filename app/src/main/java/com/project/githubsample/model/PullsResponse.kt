package com.project.githubsample.model

import com.google.gson.annotations.SerializedName

data class PullItem(
    @SerializedName("number")
    val number: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("closed_at")
    val closedAt: String
)