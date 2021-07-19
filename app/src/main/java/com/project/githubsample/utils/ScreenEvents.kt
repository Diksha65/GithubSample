package com.project.githubsample.utils

enum class ScreenEvents(val value: String) {
    ShowProgressDialog("ShowProgressDialog"),
    DismissProgressDialog("DismissProgressDialog"),
    ShowRetry("ShowRetry")
}

enum class ScreenPaginationEvents(val value: String) {
    ShowRowLoader("ShowRowLoader"),
    HideRowLoader("HideRowLoader"),
    ShowToast("ShowToast")
}