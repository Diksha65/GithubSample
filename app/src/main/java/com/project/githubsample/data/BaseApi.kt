package com.project.githubsample.data

import android.util.Log
import com.project.githubsample.utils.Result
import com.project.githubsample.utils.isNotNull
import retrofit2.Response

open class BaseApi {

    companion object {
        private const val TAG = "BaseApi"
    }

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {

        val response = call()

        return if (response.isSuccessful && response.body().isNotNull()) {
            Log.d(TAG, "Result.Success=[${response.body()!!}]")
            Result.Success(response.body()!!)

        } else if (response.errorBody().isNotNull()) {
            Log.d(TAG, "Result.Error=[${response.errorBody()!!}]")
            Result.Error(Exception(response.errorBody().toString()))

        } else {
            Result.Error(
                exception = Exception("Something went wrong")
            )
        }
    }
}