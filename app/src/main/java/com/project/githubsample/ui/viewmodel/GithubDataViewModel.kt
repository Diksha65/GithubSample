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
import com.project.githubsample.ui.adapter.RepoCardType
import com.project.githubsample.ui.adapter.ReposModelType
import com.project.githubsample.utils.Result
import com.project.githubsample.utils.ScreenEvents
import com.project.githubsample.utils.ScreenPaginationEvents
import com.project.githubsample.utils.fastLazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException

class GithubDataViewModel : ViewModel() {

    companion object {
        private const val TAG = "GithubDataViewModel"
        private const val PER_PAGE_SIZE = 20
        private const val ITEM_FETCH_THRESHOLD = 2
    }

    private var isLoading = false
    private var hasNext = false
    var currentPage = 1
        private set

    private val _repos = MutableLiveData<List<ReposModelType>>()
    val repos: LiveData<List<ReposModelType>>
        get() = _repos

    private val _prs = MutableLiveData<List<PullItem>>()
    val prs: LiveData<List<PullItem>>
        get() = _prs

    private val _events = MutableLiveData<Pair<ScreenEvents, Any?>>()
    val events: LiveData<Pair<ScreenEvents, Any?>>
        get() = _events

    private val _paginationEvents = MutableLiveData<Pair<ScreenPaginationEvents, Any?>>()
    val paginationEvents: LiveData<Pair<ScreenPaginationEvents, Any?>>
        get() = _paginationEvents

    private val repository by fastLazy {
        Module.repository
    }

    fun getRepositoriesList(userName: String, totalSize: Int) {
        Log.v(TAG, "getRepositoriesList:: userName:$userName")
        viewModelScope.launch {
            repository.getAllRepos(
                userName = userName,
                page = currentPage,
                perPage = PER_PAGE_SIZE
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Success: $result")
                        isLoading = false
                        if (currentPage == 1) {
                            _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                        } else {
                            _paginationEvents.value =
                                Pair(ScreenPaginationEvents.HideRowLoader, null)
                        }
                        hasNext = (currentPage * PER_PAGE_SIZE) < totalSize
                        currentPage += 1
                        _repos.value = getRepoList(result.data)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Error: $result")
                        if (currentPage == 1) {
                            _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                            handleErrorResponse(result.exception)
                        } else {
                            _paginationEvents.value =
                                Pair(ScreenPaginationEvents.HideRowLoader, null)
                            _paginationEvents.value = Pair(
                                ScreenPaginationEvents.ShowToast,
                                R.string.something_went_wrong
                            )
                        }

                    }
                    is Result.Loading -> {
                        Log.d(TAG, "Loading: $result")
                        if (currentPage == 1) {
                            _events.value = Pair(ScreenEvents.ShowProgressDialog, null)
                        } else {
                            _paginationEvents.value =
                                Pair(ScreenPaginationEvents.ShowRowLoader, null)
                        }
                    }
                }
            }
        }
    }

    fun getClosedPRsList(owner: String, repo: String) {
        Log.v(TAG, "getPRsList:: owner:$owner repo:$repo")
        viewModelScope.launch {
            repository.getClosedPRs(
                owner = owner,
                repo = repo
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Success: $result")
                        _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                        _prs.value = result.data
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Error: $result")
                        _events.value = Pair(ScreenEvents.DismissProgressDialog, null)
                        handleErrorResponse(result.exception)
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "Loading: $result")
                        _events.value = Pair(ScreenEvents.ShowProgressDialog, null)
                    }
                }
            }
        }
    }

    private fun handleErrorResponse(exception: Exception) {
        val errorMessage: Any? = when (exception) {
            is ConnectException -> R.string.network_error
            is UnknownHostException -> R.string.unknown_host_error
            else -> exception.message
        }
        _events.value = Pair(ScreenEvents.ShowRetry, errorMessage ?: R.string.something_went_wrong)
    }

    fun loadNextPage(
        userName: String,
        currentItems: Int,
        scrollItems: Int,
        totalItems: Int,
        totalPageSize: Int
    ) {
        if (!isLoading && hasNext
            && (currentItems + scrollItems + ITEM_FETCH_THRESHOLD) >= totalItems
            && scrollItems >= 0
        ) {
            getRepositoriesList(userName, totalPageSize)
        }
    }

    private fun getRepoList(data: List<RepositoryItem>): List<ReposModelType> {
        val list: MutableList<ReposModelType> = mutableListOf()
        data.forEach {
            list.add(RepoCardType(it))
        }
        return list
    }
}

