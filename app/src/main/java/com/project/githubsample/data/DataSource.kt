package com.project.githubsample.data

import com.project.githubsample.model.PullsResponse
import com.project.githubsample.model.RepoResponse
import com.project.githubsample.model.UserResponse
import com.project.githubsample.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import java.lang.Exception

interface DataSource {
    fun getUsersData(userName: String): Flow<Result<UserResponse>>
    fun getAllRepos(userName: String): Flow<Result<RepoResponse>>
    fun getAllPRs(owner: String, repo: String): Flow<Result<PullsResponse>>
}

class DataSourceImpl(
    private val api: Api
) : DataSource, BaseApi() {

    override fun getUsersData(userName: String): Flow<Result<UserResponse>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getUsersData(userName) }
            emit(result)
        }.catch {
            emit(Result.Error(Exception(it)))
        }
    }

    override fun getAllRepos(userName: String): Flow<Result<RepoResponse>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getAllRepos(userName) }
            emit(result)
        }.catch {
            emit(Result.Error(Exception(it)))
        }
    }

    override fun getAllPRs(owner: String, repo: String): Flow<Result<PullsResponse>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getAllPRs(owner, repo) }
            emit(result)
        }.catch {
            emit(Result.Error(Exception(it)))
        }
    }

}