package com.project.githubsample.data

import android.util.Log
import com.project.githubsample.model.PullsResponse
import com.project.githubsample.model.RepoItem
import com.project.githubsample.model.RepoResponse
import com.project.githubsample.model.UserResponse
import com.project.githubsample.utils.BaseApi
import com.project.githubsample.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import java.lang.Exception

interface DataSource {
    fun getUsersData(userName: String): Flow<Result<UserResponse>>
    fun getAllRepos(userName: String): Flow<Result<List<RepoItem>>>
    fun getAllPRs(owner: String, repo: String): Flow<Result<PullsResponse>>
}

class DataSourceImpl(
    private val api: Api
) : DataSource, BaseApi() {

    companion object {
        private const val TAG = "DataSource"
    }

    override fun getUsersData(userName: String): Flow<Result<UserResponse>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getUsersData(userName) }
            Log.d(TAG, "Success: $result")
            emit(result)
        }.catch {
            Log.e(TAG, "Error: $it")
            emit(Result.Error(Exception(it)))
        }
    }

    override fun getAllRepos(userName: String): Flow<Result<List<RepoItem>>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getAllRepos(userName) }
            Log.d(TAG, "Success: $result")
            emit(result)
        }.catch {
            Log.e(TAG, "Error: $it")
            emit(Result.Error(Exception(it)))
        }
    }

    override fun getAllPRs(owner: String, repo: String): Flow<Result<PullsResponse>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getAllPRs(owner, repo) }
            Log.d(TAG, "Success: $result")
            emit(result)
        }.catch {
            Log.e(TAG, "Error: $it")
            emit(Result.Error(Exception(it)))
        }
    }

}