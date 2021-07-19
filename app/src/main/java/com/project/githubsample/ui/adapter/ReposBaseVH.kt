package com.project.githubsample.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.githubsample.R
import com.project.githubsample.utils.SavedPreference
import com.project.githubsample.utils.isNotNull
import kotlinx.android.synthetic.main.repo_item.view.*

abstract class BaseVH(val view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: ReposModelType, pref: SavedPreference, onRepoClicked: (String) -> Unit)
}

class ReposVH(view: View) : BaseVH(view) {

    private val title = view.repoTitle
    private val description = view.repoDescription
    private val createdAt = view.createdAt
    private val updatedAt = view.updatedAt
    private val language = view.language
    private val avatarImage = view.avatarImage
    private val userName = view.userName

    override fun bind(
        item: ReposModelType,
        pref: SavedPreference,
        onRepoClicked: (String) -> Unit
    ) {
        val data = (item as RepoCardType).item
        title.text = data.name

        description.apply {
            if (data.description.isNotNull()) {
                visibility = View.VISIBLE
                text = data.description
            } else {
                visibility = View.GONE
            }
        }

        createdAt.text = view.context.getString(R.string.created_at, data.createdAt)
        updatedAt.text = view.context.getString(R.string.updated_at, data.updatedAt)

        language.apply {
            if (data.language.isNotNull()) {
                visibility = View.VISIBLE
                text = data.language
            } else {
                visibility = View.GONE
            }
        }

        view.setOnClickListener {
            onRepoClicked(data.name)
        }

        pref.getUserResponse()?.let {
            Glide.with(view.context)
                .load(it.avatarUrl)
                .placeholder(R.drawable.placeholder_profile_image)
                .into(avatarImage)

            userName.text = it.name
        }
    }
}

class LoadingVH(view: View) : BaseVH(view) {
    override fun bind(
        item: ReposModelType,
        pref: SavedPreference,
        onRepoClicked: (String) -> Unit
    ) {
        //Nothing to be done here
    }

}