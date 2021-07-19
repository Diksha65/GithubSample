package com.project.githubsample.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.githubsample.R
import com.project.githubsample.utils.SavedPreference

class ReposAdapter(
    private val onRepoClicked: ((String) -> Unit),
    private val pref: SavedPreference
) : RecyclerView.Adapter<BaseVH>() {

    companion object {
        private const val TAG = "ReposAdapter"
        private const val TYPE_REPO_CARD = R.layout.repo_item
        private const val TYPE_LOADING_CARD = R.layout.layout_list_loading_item
    }

    private lateinit var layoutInflater: LayoutInflater
    private val reposList = ArrayList<ReposModelType>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        if (!::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)

        val layout = layoutInflater.inflate(viewType, parent, false)

        return when (viewType) {
            TYPE_REPO_CARD -> ReposVH(layout)
            TYPE_LOADING_CARD -> LoadingVH(layout)
            else -> {
                Log.e(TAG, "onCreateViewHolder card type not present $viewType")
                throw IllegalArgumentException("onCreateViewHolder card type not present $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        holder.bind(reposList[position], pref, onRepoClicked)
    }

    override fun getItemCount(): Int = reposList.size

    override fun getItemViewType(position: Int): Int {

        return when (reposList[position]) {
            is RepoCardType -> TYPE_REPO_CARD
            is LoadingCardType -> TYPE_LOADING_CARD
            else -> {
                Log.e(TAG, "getItemViewType card type not present ${reposList[position]}")
                throw IllegalArgumentException("getItemViewType card type not present ${reposList[position]}")
            }
        }
    }

    fun submitList(newRepoList: List<ReposModelType>) {
        reposList.clear()
        reposList.addAll(newRepoList)
        notifyDataSetChanged()
    }

    fun addLoadingItem() {
        reposList.add(LoadingCardType)
        notifyDataSetChanged()
    }

    fun removeLoadingItem() {
        for (position in reposList.indices.reversed()) {
            if (reposList[position] is LoadingCardType) {
                reposList.removeAt(position)
                notifyDataSetChanged()
                return
            }
        }
    }
}