package com.project.githubsample.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubsample.R
import com.project.githubsample.custom.ProgressDialog
import com.project.githubsample.model.RepositoryItem
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
                            is Int -> retryMessage.text = getString(data)
                            is String -> retryMessage.text = data
                            else -> retryMessage.text = getString(R.string.something_went_wrong)
                        }
                    }
                    retryMessage.visibility = View.VISIBLE
                    retryButton.text = getString(R.string.retry)
                    retryButton.visibility = View.VISIBLE

                    recyclerView.visibility = View.GONE
                    noResultsText.visibility = View.GONE
                }
            }
        }
    }

    private val reposObserver: Observer<List<RepositoryItem>> by fastLazy {
        Observer<List<RepositoryItem>> {
            showSuccessScreen(it)
        }
    }

    override fun getLogTag(): String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        viewModel.events.observe(this, eventObserver)
        viewModel.repos.observe(this, reposObserver)

        viewModel.getRepositoriesList("Diksha65") //ToDo Diksha - Remove the hardcode

        retryButton.setOnClickListener {
            viewModel.getRepositoriesList("Diksha65")
        }

    }

    override fun onPause() {
        progressDialog?.hideProgress()
        super.onPause()
    }

    private fun showSuccessScreen(data: List<RepositoryItem>) {
        retryButton.visibility = View.GONE
        retryMessage.visibility = View.GONE

        if (data.isEmpty()) {
            noResultsText.visibility = View.VISIBLE
            noResultsText.text = getString(R.string.no_public_repos)
            recyclerView.visibility = View.GONE
        } else {
            noResultsText.visibility = View.GONE
            recyclerView.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@ReposActivity)

                adapter = ReposAdapter(
                    items = data,
                    onRepoClicked = {
                        Log.d(TAG, "$it clicked")
                        ClosedPRsActivity.startActivity(
                            context = this@ReposActivity,
                            owner = "Diksha65", //ToDO Diksha Change this
                            repo = it
                        )
                    }
                )
            }
        }
    }
}