package com.project.githubsample.ui.screen

import android.os.Bundle
import com.project.githubsample.utils.BaseActivity
import com.project.githubsample.utils.SavedPreference
import com.project.githubsample.utils.isNotNull

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun getLogTag(): String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savedPreference = SavedPreference(this)
        if (savedPreference.loggedInUser().isNotNull()) {
            ReposActivity.startActivity(this, savedPreference.loggedInUser()!!)
        } else {
            LoginActivity.startActivity(this)
        }
    }
}