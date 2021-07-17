package com.project.githubsample.data

import com.project.githubsample.model.PullsResponse
import com.project.githubsample.model.RepoItem
import com.project.githubsample.model.RepoResponse
import com.project.githubsample.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    companion object {
        private const val GET_USER_DATA = "/users/{username}"
        private const val GET_ALL_REPOS = "/users/{username}/repos"
        private const val GET_ALL_PRS = "/repos/{owner}/{repo}/pulls"
    }

    @GET(GET_USER_DATA)
    suspend fun getUsersData(
        @Path("username") userName: String
    ): Response<UserResponse>

    @GET(GET_ALL_REPOS)
    suspend fun getAllRepos(
        @Path("username") userName: String
    ): Response<List<RepoItem>>

    @GET(GET_ALL_PRS)
    suspend fun getAllPRs(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<PullsResponse>
}