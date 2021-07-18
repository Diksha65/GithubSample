package com.project.githubsample.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.githubsample.R
import com.project.githubsample.model.RepositoryItem
import com.project.githubsample.utils.isNotNull
import kotlinx.android.synthetic.main.repo_item.view.*

class ReposAdapter(
    private val items: List<RepositoryItem>,
    private val onRepoClicked: ((String) -> Unit)
) : RecyclerView.Adapter<ReposAdapter.ReposVH>() {

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposVH {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.repo_item, parent, false)
        return ReposVH(view)
    }

    override fun onBindViewHolder(holder: ReposVH, position: Int) {
        holder.bind(items[position], onRepoClicked)
    }

    override fun getItemCount(): Int = items.size

    inner class ReposVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title = itemView.repoTitle
        private val description = itemView.repoDescription
        private val createdAt = itemView.createdAt
        private val updatedAt = itemView.updatedAt
        private val language = itemView.language

        fun bind(item: RepositoryItem, onRepoClicked: (String) -> Unit) {
            title.text = item.name

            description.apply {
                if (item.description.isNotNull()) {
                    visibility = View.VISIBLE
                    text = item.description
                } else {
                    visibility = View.GONE
                }
            }

            Log.e("**********************", "${item.createdAt} ${item.updatedAt}") //ToDO Diksha Check
            createdAt.text = itemView.context.getString(R.string.created_at, item.createdAt)
            updatedAt.text = itemView.context.getString(R.string.updated_at, item.updatedAt)

            language.apply {
                if (item.language.isNotNull()) {
                    visibility = View.VISIBLE
                    text = item.language
                } else {
                    visibility = View.GONE
                }
            }

            itemView.setOnClickListener {
                onRepoClicked(item.name)
            }
        }
    }
}