package com.project.githubsample.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubsample.R
import com.project.githubsample.custom.ProgressDialog
import com.project.githubsample.model.RepoItem
import com.project.githubsample.model.RepoResponse
import com.project.githubsample.ui.adapter.ReposAdapter
import com.project.githubsample.ui.viewmodel.GithubDataViewModel
import com.project.githubsample.utils.*
import kotlinx.android.synthetic.main.recycler_view.*

class ReposActivity : BaseActivity() {

    companion object {
        private const val TAG = "ReposActivity"
    }

    private var progressDialog: ProgressDialog? = null

    private val viewModel: GithubDataViewModel by fastLazy {
        ViewModelProvider(this).get(GithubDataViewModel::class.java)
    }

    private val eventObserver: Observer<Pair<ScreenEvents, Any?>> by fastLazy {
        Observer<Pair<ScreenEvents, Any?>> {
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
                ScreenEvents.ShowToast -> {
                    it.second?.let { msg ->
                        when (msg) {
                            is Int -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                            is String -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                            else -> Log.e(TAG, "got toast event with invalid data")
                        }
                    }
                }
                ScreenEvents.ShowRetry -> {
                    retryButton.text = getString(R.string.retry)
                    retryButton.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    noReposText.visibility = View.GONE
                }
                ScreenEvents.ShowSuccess -> {
                    if (it.second.isNotNull() && it.second is List<*>) {
                        showSuccessScreen(it.second as List<RepoItem>)
                    }
                }
            }
        }
    }

    override fun getLogTag(): String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        viewModel.events.observe(this, eventObserver)
        viewModel.getRepositoriesList("Diksha65") //ToDo Diksha - Remove the hardcode

        retryButton.setOnClickListener {
            viewModel.getRepositoriesList("Diksha65")
        }
    }

    override fun onPause() {
        progressDialog?.hideProgress()
        super.onPause()
    }

    private fun showSuccessScreen(data: List<RepoItem>) {
        retryButton.visibility = View.GONE

        if(data.isEmpty()) {
            noReposText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noReposText.visibility = View.GONE
            recyclerView.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@ReposActivity)

                adapter = ReposAdapter(
                    items = data,
                    onRepoClicked = {
                        Log.e(TAG, "$it clicked")
                    }
                )
            }
        }
    }
}