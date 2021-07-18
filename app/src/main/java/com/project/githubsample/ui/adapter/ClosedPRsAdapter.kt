package com.project.githubsample.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.githubsample.R
import com.project.githubsample.model.PullItem
import com.project.githubsample.utils.isNotNull
import kotlinx.android.synthetic.main.pr_item.view.*

class ClosedPRsAdapter(
    private val items: List<PullItem>,
    private val onPrClicked: ((String) -> Unit)
) : RecyclerView.Adapter<ClosedPRsAdapter.PullsVH>() {

    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullsVH {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.pr_item, parent, false)
        return PullsVH(view)
    }

    override fun onBindViewHolder(holder: PullsVH, position: Int) {
        holder.bind(items[position], onPrClicked)
    }

    override fun getItemCount(): Int = items.size

    inner class PullsVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val prTitle = itemView.prTitle
        private val prDescription = itemView.prDescription
        private val createdAt = itemView.createdAt
        private val closedAt = itemView.closedAt

        fun bind(item: PullItem, onPrClicked: (String) -> Unit) {

            prTitle.text = item.number.toString() + ": " + item.title

            prDescription.apply {
                if (item.body.isNotNull()) {
                    visibility = View.VISIBLE
                    text = item.body
                } else {
                    visibility = View.GONE
                }
            }

            Log.e(
                "**********************",
                "${item.createdAt} ${item.closedAt}"
            ) //ToDO Diksha Check
            createdAt.text = itemView.context.getString(R.string.created_at, item.createdAt)
            closedAt.text = itemView.context.getString(R.string.updated_at, item.closedAt)

            itemView.setOnClickListener {
                onPrClicked(item.title)
            }
        }
    }
}