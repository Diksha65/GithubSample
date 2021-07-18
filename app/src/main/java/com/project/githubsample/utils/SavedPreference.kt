package com.project.githubsample.utils

import android.content.Context
import android.content.SharedPreferences

class SavedPreference(context: Context) {

    companion object {
        private const val PREF_NAME = "GITHUB_SAMPLE"
        private const val LOGGED_IN_USER_NAME = "LOGGED_IN_USER_NAME"
    }

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
        editor.apply()
    }

    fun loginUser(userName: String) {
        editor.putString(LOGGED_IN_USER_NAME, userName)
        editor.commit()
    }

    fun loggedInUser(): String? {
        return pref.getString(LOGGED_IN_USER_NAME, null)
    }
}