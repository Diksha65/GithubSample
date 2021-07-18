package com.project.githubsample.data

import com.project.githubsample.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    companion object {
        private const val GET_USER_DATA = "/users/{username}"
        private const val GET_ALL_REPOS = "/users/{username}/repos"
        private const val GET_CLOSED_PRS = "/repos/{owner}/{repo}/pulls"
    }

    @GET(GET_USER_DATA)
    suspend fun getUsersData(
        @Path("username") userName: String
    ): Response<UserResponse>

    @GET(GET_ALL_REPOS)
    suspend fun getAllRepos(
        @Path("username") userName: String,
        @Query("sort") sort: String = "created"
    ): Response<List<RepositoryItem>>

    @GET(GET_CLOSED_PRS)
    suspend fun getClosedPRs(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("state") state: String = "closed",
        @Query("direction") direction: String = "asc"
    ): Response<List<PullItem>>
}