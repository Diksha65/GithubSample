package com.project.githubsample.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.githubsample.R
import com.project.githubsample.custom.ProgressDialog
import com.project.githubsample.model.PullItem
import com.project.githubsample.ui.adapter.ClosedPRsAdapter
import com.project.githubsample.ui.viewmodel.GithubDataViewModel
import com.project.githubsample.utils.BaseActivity
import com.project.githubsample.utils.ScreenEvents
import com.project.githubsample.utils.fastLazy
import com.project.githubsample.utils.isNull
import kotlinx.android.synthetic.main.recycler_view.*

class ClosedPRsActivity : BaseActivity() {

    companion object {
        private const val TAG = "ClosedPRsActivity"
        private const val OWNER = "OWNER"
        private const val REPO = "REPO"

        fun startActivity(context: Context, owner: String, repo: String) {
            val intent = Intent(context, ClosedPRsActivity::class.java).apply {
                putExtra(OWNER, owner)
                putExtra(REPO, repo)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var owner: String
    private lateinit var repo: String
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

    private val prsObserver: Observer<List<PullItem>> by fastLazy {
        Observer<List<PullItem>> {
            showSuccessScreen(it)
        }
    }

    override fun getLogTag(): String = TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        owner = intent.getStringExtra(OWNER)!!
        repo = intent.getStringExtra(REPO)!!

        viewModel.events.observe(this, eventObserver)
        viewModel.prs.observe(this, prsObserver)

        viewModel.getClosedPRsList(owner, repo)

        retryButton.setOnClickListener {
            viewModel.getClosedPRsList(owner, repo)
        }

    }

    override fun onPause() {
        progressDialog?.hideProgress()
        super.onPause()
    }

    private fun showSuccessScreen(data: List<PullItem>) {
        retryButton.visibility = View.GONE
        retryMessage.visibility = View.GONE

        if (data.isEmpty()) {
            noResultsText.visibility = View.VISIBLE
            noResultsText.text = getString(R.string.no_closed_prs)
            recyclerView.visibility = View.GONE
        } else {
            noResultsText.visibility = View.GONE
            recyclerView.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@ClosedPRsActivity)

                adapter = ClosedPRsAdapter(
                    items = data,
                    onPrClicked = {
                        Log.e(TAG, "$it clicked") //ToDos Diksha Change this
                    }
                )
            }
        }
    }
}