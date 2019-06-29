package com.larvaesoft.opengst.ui.updates

import android.content.Context

interface GupdateInterface {
    fun showMessage()
    fun getContextFor(): Context
    fun setUpAdapter(feedAdapter: FeedAdapter)
    fun showProgress()
    fun hideProgress()
    fun showNoUpdatesAvailable()
}