package com.project.githubsample.custom

import android.app.Dialog
import android.content.Context
import android.view.Window
import androidx.annotation.StringRes
import com.project.githubsample.R
import kotlinx.android.synthetic.main.progress_dialog.*

class ProgressDialog {
    private var mDialog: Dialog? = null

    fun showProgress(
        context: Context,
        @StringRes title: Int,
        @StringRes message: Int
    ) {

        mDialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.progress_dialog)
            progressTitle.text = context.getString(title)
            progressDesc.text = context.getString(message)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    fun hideProgress() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }
}