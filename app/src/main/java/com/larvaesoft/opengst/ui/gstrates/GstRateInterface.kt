package com.larvaesoft.opengst.ui.gstrates

import android.content.Context
import com.larvaesoft.opengst.model.Item

interface GstRateInterface {
    fun showMessage(s: String)
    fun setUpAdapter(adapter: ResultAdapter)
    fun showResult()
    fun showNoResult()
    fun hideKeyboard()
    fun showTotalResults(size:Int)
    fun showDetailDialog(item: Item)
    fun getMyContext(): Context
    fun logSearchQuery(query: String?)
}