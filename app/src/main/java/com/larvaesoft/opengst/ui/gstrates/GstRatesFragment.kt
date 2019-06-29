package com.larvaesoft.opengst.ui.gstrates


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.larvaesoft.opengst.GSTApplication

import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.base.BaseFragment
import com.larvaesoft.opengst.model.Item
import com.larvaesoft.opengst.utils.GSTHelper
import kotlinx.android.synthetic.main.fragment_gst_rates.*
import kotlinx.android.synthetic.main.rate_detail_dialog.view.*
import org.jetbrains.anko.share
import org.jetbrains.anko.toast
import timber.log.Timber
import javax.inject.Inject


class GstRatesFragment : BaseFragment(), GstRateInterface {
    @Inject lateinit var presenter: GstRatePresenter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_gst_rates, container, false)
        DaggerGstRateComponent.builder()
                .appComponent(GSTApplication.component)
                .gstRateModule(GstRateModule(this))
                .build()
                .inject(this)
        return view
    }

    private fun searchFor(query: String?) {
        presenter.search(query)
    }


    private lateinit var request: AdRequest

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        search_view.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchFor(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        feedRecyclerView.layoutManager = LinearLayoutManager(activity)
        val decorator = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        feedRecyclerView.addItemDecoration(decorator)
        MobileAds.initialize(activity.applicationContext, getString(R.string.app_id))
        val request = AdRequest.Builder().build()
        adViewBottom.loadAd(request)
        presenter.init()

    }

    override fun showMessage(s: String) {
        activity.toast(s)
    }

    override fun setUpAdapter(adapter: ResultAdapter) {
        feedRecyclerView.adapter = adapter
    }

    override fun showResult() {
        feedRecyclerView.visibility = View.VISIBLE
        empty_view.visibility = View.GONE
        hideKeyboard()
    }

    override fun showNoResult() {
        feedRecyclerView.visibility = View.GONE
        empty_view.visibility = View.VISIBLE
        total_wrapper.visibility = View.GONE
        hideKeyboard()
    }

    override fun hideKeyboard() {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun showTotalResults(size: Int) {
        total_wrapper.visibility = View.VISIBLE
        total_result_text.text = "$size results"
    }

    override fun showDetailDialog(item: Item) {
        val view = activity.layoutInflater.inflate(R.layout.rate_detail_dialog, null)
        view.description.text = item.description
        view.rate.text = "${item.rate}%"
        view.type.text = item.type
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        builder.setTitle(GSTHelper.getHsn(item.hsn))
        builder.setPositiveButton("Share", { _, _ ->
            shareItemDetail(item)
        })
        builder.setNegativeButton("Close", { dialog, _ ->
            dialog.dismiss()
        })
        builder.show()
    }

    private fun shareItemDetail(item: Item) {
        val text = "${GSTHelper.getHsn(item.hsn)} is for ${item.type.capitalize()} having GST Rate of ${item.rate}%. Explore more at OpenGST (GST guide and calculator) https://goo.gl/GwgdtH"
        activity.share(text, "Share")
    }

    override fun getMyContext(): Context {
        return activity
    }

    override fun logSearchQuery(query: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM,query)
        fireBaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,bundle)
    }
}
