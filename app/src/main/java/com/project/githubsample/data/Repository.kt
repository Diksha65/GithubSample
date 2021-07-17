package com.project.githubsample.data

import com.project.githubsample.model.PullsResponse
import com.project.githubsample.model.RepoResponse
import com.project.githubsample.model.UserResponse
import com.project.githubsample.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Repository {
    fun getUsersData(userName: String): Flow<Result<UserResponse>>
    fun getAllRepos(userName: String): Flow<Result<RepoResponse>>
    fun getAllPRs(owner: String, repo: String): Flow<Result<PullsResponse>>
}

class RepositoryImpl(
    private val dataSource: DataSource
) : Repository {
    override fun getUsersData(userName: String): Flow<Result<UserResponse>> {
        return dataSource.getUsersData(userName).map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }

    override fun getAllRepos(userName: String): Flow<Result<RepoResponse>> {
        return dataSource.getAllRepos(userName).map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }

    override fun getAllPRs(owner: String, repo: String): Flow<Result<PullsResponse>> {
        return dataSource.getAllPRs(owner, repo).map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }

}