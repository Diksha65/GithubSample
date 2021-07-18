package com.project.githubsample.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.project.githubsample.R
import com.project.githubsample.ui.viewmodel.LoginViewModel
import com.project.githubsample.utils.BaseActivity
import com.project.githubsample.utils.afterTextChanged
import com.project.githubsample.utils.fastLazy
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private val viewModel: LoginViewModel by fastLazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun getLogTag(): String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        setScreen()
    }

    private fun setScreen() {
        if(viewModel.getUserName().isNotEmpty()) {
            nameEditText.setText(viewModel.getUserName())
        }

        nameEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                nameEditText.background = getDrawable(R.drawable.edit_text_rounded_border_active)
                nameEditText.hint = ""
            }
        }

        nameEditText.afterTextChanged {
            viewModel.saveUserName(it)
            if(it.isNotEmpty()) {
                loginButton.setBackgroundColor(getColor(R.color.grey_dark))
            } else {
                loginButton.setBackgroundColor(getColor(R.color.grey_lightest))
            }
        }

        loginButton.setOnClickListener {
            val userName = nameEditText.text.toString()
            if(userName.isEmpty()) {
                Toast.makeText(this, R.string.please_enter_name, Toast.LENGTH_SHORT).show()
            } else {
                ReposActivity.startActivity(this, userName)
            }
        }


    }
}