package com.project.githubsample.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.project.githubsample.R
import com.project.githubsample.custom.ProgressDialog
import com.project.githubsample.model.RepositoryItem
import com.project.githubsample.ui.adapter.ReposAdapter
import com.project.githubsample.ui.viewmodel.GithubDataViewModel
import com.project.githubsample.utils.*
import kotlinx.android.synthetic.main.activity_repo.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import kotlinx.android.synthetic.main.recycler_view.*


class ReposActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val TAG = "ReposActivity"

        private const val USER_NAME = "USER_NAME"

        fun startActivity(context: Context, userName: String) {
            val intent = Intent(context, ReposActivity::class.java).apply {
                putExtra(USER_NAME, userName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userName: String
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
        setContentView(R.layout.activity_repo)
        setSupportActionBar(toolbar)

        setUpNavigationDrawer()

        userName = intent.getStringExtra(USER_NAME)!!

        viewModel.events.observe(this, eventObserver)
        viewModel.repos.observe(this, reposObserver)

        viewModel.getRepositoriesList(userName)

        retryButton.setOnClickListener {
            viewModel.getRepositoriesList(userName)
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
                            owner = userName,
                            repo = it
                        )
                    }
                )
            }
        }
    }

    private fun setUpNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val pref = SavedPreference(this)
        val headerView: View = nav_view.getHeaderView(0)

        Log.e(TAG, "$headerView")

        val userResponse = pref.getUserResponse()

        Log.e(TAG, "userResponse $userResponse")
        userResponse?.let {
            Glide.with(this)
                .load(it.avatarUrl)
                .placeholder(R.drawable.placeholder_profile_image)
                .into(headerView.avatarView)

            headerView.githubLoginName.text = it.login
            headerView.githubName.text = it.name

            if(it.company.isNotNull()) {
                headerView.company.text = it.company
                headerView.company.visibility = View.VISIBLE
            } else {
                headerView.company.visibility = View.GONE
            }

            headerView.publicRepos.text = getString(R.string.public_repos, it.publicRepos)
            headerView.followers_following.text = getString(R.string.followers_following, it.followers, it.following)
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_logout -> {
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                finish()
                val pref = SavedPreference(this)
                pref.setUserResponse(null)
                pref.setUserName(null)
                LoginActivity.startActivity(this)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}