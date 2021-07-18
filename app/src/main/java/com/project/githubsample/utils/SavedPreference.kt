package com.project.githubsample.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.project.githubsample.di.Module
import com.project.githubsample.model.UserResponse
import com.squareup.moshi.JsonAdapter

class SavedPreference(context: Context) {

    companion object {
        private const val PREF_NAME = "GITHUB_SAMPLE"
        private const val LOGGED_IN_USER_NAME = "LOGGED_IN_USER_NAME"
        private const val USER_RESPONSE = "USER_RESPONSE"
    }

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
        editor.apply()
    }

    fun setUserName(userName: String?) {
        editor.putString(LOGGED_IN_USER_NAME, userName)
        editor.commit()
    }

    fun loggedInUser(): String? {
        return pref.getString(LOGGED_IN_USER_NAME, null)
    }

    fun setUserResponse(data: UserResponse?) {
        Log.e("Pref", "setUserResponse $data")
        if (data.isNull()) {
            editor.putString(USER_RESPONSE, null)
        } else {
            val userResponseAdapter: JsonAdapter<UserResponse> =
                Module.moshi.adapter(UserResponse::class.java)
            Log.e("Pref", "setUserResponse 2 ${userResponseAdapter.toJson(data)}")
            editor.putString(USER_RESPONSE, userResponseAdapter.toJson(data))
            editor.commit()
        }
    }

    fun getUserResponse(): UserResponse? {
        Log.e("Pref", "getUserResponse")
        val data = pref.getString(USER_RESPONSE, null)
        if (data.isNull()) return null
        Log.e("Pref", "getUserResponse 2 $data")
        val userResponseAdapter: JsonAdapter<UserResponse> =
            Module.moshi.adapter(UserResponse::class.java)
        Log.e("Pref", "getUserResponse 3 ${userResponseAdapter.fromJson(data)}")
        return userResponseAdapter.fromJson(data!!)
    }
}