package com.project.githubsample.ui.viewmodel

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private var savedUserName: String = ""

    fun saveUserName(userName: String) {
        savedUserName = userName
    }

    fun getUserName(): String {
        return savedUserName
    }
}