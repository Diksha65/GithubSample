package com.project.githubsample.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.githubsample.R
import com.project.githubsample.di.Module
import com.project.githubsample.utils.Result
import com.project.githubsample.utils.SavedPreference
import com.project.githubsample.utils.ScreenEvents
import com.project.githubsample.utils.fastLazy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException

class LoginViewModel : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private var savedUserName: String = ""

    private val _events = MutableLiveData<Pair<ScreenEvents, Any?>>()
    val events: LiveData<Pair<ScreenEvents, Any?>>
        get() = _events

    private val repository by fastLazy {
        Module.repository
    }

    fun saveUserName(userName: String) {
        savedUserName = userName
    }

    fun getUserName(): String {
        return savedUserName
    }

    fun getUserResponse(userName: String, pref: SavedPreference, onSuccess: (() -> Unit)) {
        Log.v(TAG, "getUserResponse:: userName:$userName")
        viewModelScope.launch {
            repository.getUsersData(userName).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "Success: $result")
                        pref.setUserResponse(result.data)
                        onSuccess()
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