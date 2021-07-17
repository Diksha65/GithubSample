package com.project.githubsample.utils

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Override this function returning the logging tag from the child class
     */
    abstract fun getLogTag(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(getLogTag(), "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.v(getLogTag(), "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(getLogTag(), "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.v(getLogTag(), "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(getLogTag(), "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(getLogTag(), "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.v(getLogTag(), "onRestart")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.v(getLogTag(), "onBackPressed")
    }
}