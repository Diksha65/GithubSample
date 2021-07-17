package com.project.githubsample.model

import com.squareup.moshi.Json

data class PullsResponse(
    @Json(name = "data")
    val pullsResponse: List<PullItem>
)

data class PullItem(
    @Json(name = "id")
    val id: Int,

    @Json(name = "node_id")
    val nodeId: String,

    @Json(name = "number")
    val number: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "state")
    val state: String,

    @Json(name = "body")
    val body: String,

    @Json(name = "created_at")
    val createdAt: String,

    @Json(name = "closed_at")
    val closedAt: String
)