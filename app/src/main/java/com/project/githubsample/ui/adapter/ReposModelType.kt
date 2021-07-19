package com.project.githubsample.ui.adapter

import com.project.githubsample.model.RepositoryItem

sealed class ReposModelType

data class RepoCardType(val item: RepositoryItem) : ReposModelType()
object LoadingCardType : ReposModelType()