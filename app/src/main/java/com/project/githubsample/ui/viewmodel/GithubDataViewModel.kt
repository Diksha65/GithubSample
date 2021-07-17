package com.project.githubsample.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.githubsample.R
import com.project.githubsample.di.Module
import com.project.githubsample.model.PullsResponse
import com.project.githubsample.model.RepoResponse
import com.project.githubsample.utils.Result
import com.project.githubsample.utils.ScreenEvents
import com.project.githubsample.utils.fastLazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException

class GithubDataViewModel : ViewModel() {

    companion object {
        private const val TAG = "GithubDataViewModel"
    }

    private val _repos = MutableLiveData<RepoResponse>()
    val repos: LiveData<RepoResponse>
        get() = _repos

    private val _prs = MutableLiveData<PullsResponse>()
    val prs: LiveData<PullsResponse>
        get() = _prs

    private val _events = MutableLiveData<Pair<ScreenEvents, Any?>>()
    val events: LiveData<Pair<ScreenEvents, Any?>>
        get() = _events

    private val repository by fastLazy {
        Module.repository
    }

    fun getRepositoriesList(userName: String) {
        Log.v(TAG, "getRepositoriesList:: userName:$userName")
        viewModelScope.launch {
            repository.getAllRepos(
                userName = userName
            ).collect {
                handleResponse(it)
            }
        }
    }

    fun getPRsList(owner: String, repo: String) {
        Log.v(TAG, "getPRsList:: owner:$owner repo:$repo")
        viewModelScope.launch {
            repository.getAllPRs(
                owner = owner,
                repo = repo
            ).collect {
                handleResponse(it)
            }
        }
    }

    private fun <T> handleResponse(result: Result<T>) {
        when (result) {
            is Result.Success -> {
                Log.d(TAG, "Success: $result")
                _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                _events.value = Pair(ScreenEvents.ShowSuccess, result.data)
            }
            is Result.Error -> {
                Log.e(TAG, "Error: $result")
                _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                _events.value = Pair(ScreenEvents.ShowRetry, null)
                handleErrorResponse(result.exception)
            }
            is Result.Loading -> {
                Log.d(TAG, "Loading: $result")
                _events.value = Pair(ScreenEvents.ShowProgressDialog, null)
            }
        }
    }

    private fun handleErrorResponse(exception: Exception) {
        val errorMessage: Any? = when (exception) {
            is ConnectException -> R.string.network_error
            is UnknownHostException -> R.string.unknown_host_error
            else -> exception.message
        }
        _events.value = Pair(ScreenEvents.ShowToast, errorMessage ?: R.string.something_went_wrong)
    }
}

