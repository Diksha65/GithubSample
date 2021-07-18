package com.project.githubsample.data

import com.project.githubsample.model.PullItem
import com.project.githubsample.model.RepositoryItem
import com.project.githubsample.model.UserResponse
import com.project.githubsample.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface Repository {
    fun getUsersData(userName: String): Flow<Result<UserResponse>>
    fun getAllRepos(userName: String): Flow<Result<List<RepositoryItem>>>
    fun getClosedPRs(owner: String, repo: String): Flow<Result<List<PullItem>>>
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

    override fun getAllRepos(userName: String): Flow<Result<List<RepositoryItem>>> {
        return dataSource.getAllRepos(userName).map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }

    override fun getClosedPRs(owner: String, repo: String): Flow<Result<List<PullItem>>> {
        return dataSource.getClosedPRs(owner, repo).map { result ->
            when (result) {
                is Result.Success -> result
                is Result.Error -> result
                is Result.Loading -> result
            }
        }
    }

}