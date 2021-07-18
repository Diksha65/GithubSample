package com.project.githubsample.data

import android.util.Log
import com.project.githubsample.model.PullItem
import com.project.githubsample.model.RepositoryItem
import com.project.githubsample.model.UserResponse
import com.project.githubsample.utils.BaseApi
import com.project.githubsample.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

interface DataSource {
    fun getUsersData(userName: String): Flow<Result<UserResponse>>
    fun getAllRepos(userName: String): Flow<Result<List<RepositoryItem>>>
    fun getClosedPRs(owner: String, repo: String): Flow<Result<List<PullItem>>>
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

    override fun getAllRepos(userName: String): Flow<Result<List<RepositoryItem>>> {
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

    override fun getClosedPRs(owner: String, repo: String): Flow<Result<List<PullItem>>> {
        return flow {
            emit(Result.Loading)
            val result = getResult { api.getClosedPRs(owner, repo) }
            Log.d(TAG, "Success: $result")
            emit(result)
        }.catch {
            Log.e(TAG, "Error: $it")
            emit(Result.Error(Exception(it)))
        }
    }

}