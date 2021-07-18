package com.project.githubsample.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.project.githubsample.R
import com.project.githubsample.custom.ProgressDialog
import com.project.githubsample.ui.viewmodel.LoginViewModel
import com.project.githubsample.utils.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    companion object {
        private const val TAG = "LoginActivity"

        fun startActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    private var progressDialog: ProgressDialog? = null

    private val viewModel: LoginViewModel by fastLazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private val eventObserver: Observer<Pair<ScreenEvents, Any?>> by fastLazy {
        Observer<Pair<ScreenEvents, Any?>> {
            Log.e(TAG, "${it.first} ${it.second}")
            when (it.first) {
                ScreenEvents.ShowProgressDialog -> {
                    if (progressDialog.isNull()) progressDialog = ProgressDialog()
                    progressDialog!!.showProgress(
                        context = this,
                        title = R.string.loading,
                        message = R.string.please_wait
                    )
                }
                ScreenEvents.DismissProgressDialog -> {
                    progressDialog?.hideProgress()
                }
                ScreenEvents.ShowRetry -> {
                    it.second?.let { data ->
                        when (data) {
                            is Int -> Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                            is String -> Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(
                                this,
                                R.string.something_went_wrong,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun getLogTag(): String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        viewModel.events.observe(this, eventObserver)
        setScreen()
    }

    private fun setScreen() {
        if (viewModel.getUserName().isNotEmpty()) {
            nameEditText.setText(viewModel.getUserName())
        }

        nameEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                nameEditText.background = getDrawable(R.drawable.edit_text_rounded_border_active)
                nameEditText.hint = ""
            }
        }

        nameEditText.afterTextChanged {
            viewModel.saveUserName(it)
            if (it.isNotEmpty()) {
                loginButton.setBackgroundColor(getColor(R.color.grey_dark))
            } else {
                loginButton.setBackgroundColor(getColor(R.color.grey_lightest))
            }
        }

        loginButton.setOnClickListener {
            val userName = nameEditText.text.toString()
            if (userName.isEmpty()) {
                Toast.makeText(this, R.string.please_enter_name, Toast.LENGTH_SHORT).show()
            } else {
                val savedPreference = SavedPreference(this)
                savedPreference.loginUser(userName)
                viewModel.getUserResponse(
                    userName = userName,
                    pref = savedPreference,
                    onSuccess = {
                        ReposActivity.startActivity(this, userName)
                    }
                )
            }
        }


    }
}