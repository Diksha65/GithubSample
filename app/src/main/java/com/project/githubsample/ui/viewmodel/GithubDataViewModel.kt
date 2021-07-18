package com.project.githubsample.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.githubsample.R
import com.project.githubsample.di.Module
import com.project.githubsample.model.PullItem
import com.project.githubsample.model.RepositoryItem
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

    private val _repos = MutableLiveData<List<RepositoryItem>>()
    val repos: LiveData<List<RepositoryItem>>
        get() = _repos

    private val _prs = MutableLiveData<List<PullItem>>()
    val prs: LiveData<List<PullItem>>
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
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Success: $result")
                        _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                        _repos.value = result.data
                    }
                    else -> handleNonSuccessResponse(result)
                }
            }
        }
    }

    fun getPRsList(owner: String, repo: String) {
        Log.v(TAG, "getPRsList:: owner:$owner repo:$repo")
        viewModelScope.launch {
            repository.getAllPRs(
                owner = owner,
                repo = repo
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Success: $result")
                        _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                        _prs.value = result.data
                    }
                    else -> handleNonSuccessResponse(result)
                }
            }
        }
    }

    private fun <T> handleNonSuccessResponse(result: Result<T>) {
        when (result) {
            is Result.Error -> {
                Log.e(TAG, "Error: $result")
                _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                val errorMessage: Any? = when (result.exception) {
                    is ConnectException -> R.string.network_error
                    is UnknownHostException -> R.string.unknown_host_error
                    else -> result.exception.message
                }
                _events.value =
                    Pair(ScreenEvents.ShowRetry, errorMessage ?: R.string.something_went_wrong)
            }
            is Result.Loading -> {
                Log.d(TAG, "Loading: $result")
                _events.value = Pair(ScreenEvents.ShowProgressDialog, null)
            }
        }
    }
}

