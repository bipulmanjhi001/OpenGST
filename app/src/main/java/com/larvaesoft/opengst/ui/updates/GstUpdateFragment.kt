package com.larvaesoft.opengst.ui.updates


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.larvaesoft.opengst.GSTApplication

import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_gst_update.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class GstUpdateFragment : BaseFragment(), GupdateInterface {
    @Inject lateinit var presenter: GupdatePresenter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_gst_update, container, false)
        DaggerGupdateComponent.builder()
                .appComponent(GSTApplication.component)
                .gupdateModule(GupdateModule(this))
                .build()
                .inject(this)
        return view
    }

    override fun showMessage() {
        activity.toast("hi")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feedRecyclerView.layoutManager = LinearLayoutManager(activity)
        presenter.loadData()
        swipe_refresh.setOnRefreshListener { presenter.loadData() }
        MobileAds.initialize(activity.applicationContext,getString(R.string.app_id))
        val request = AdRequest.Builder()
              //  .addTestDevice("8AF196582EF001C05AE6DAC416B8FB35")
                .build()
        adViewBottom.loadAd(request)
    }

    override fun getContextFor(): Context {
        return activity
    }

    override fun setUpAdapter(feedAdapter: FeedAdapter) {
        feedRecyclerView.adapter = feedAdapter
    }

    override fun showProgress() {
        empty_view.visibility = View.GONE
        swipe_refresh.isRefreshing = true
    }

    override fun hideProgress() {
        if(swipe_refresh !=null){
            swipe_refresh.isRefreshing = false
        }
    }

    override fun onPause() {
        super.onPause()
        swipe_refresh.isRefreshing =false
        presenter.disposeAll()
    }

    override fun showNoUpdatesAvailable() {
        empty_view.visibility = View.VISIBLE
    }
}
