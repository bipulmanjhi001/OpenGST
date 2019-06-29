package com.larvaesoft.opengst.ui.about


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.larvaesoft.opengst.Constants

import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.base.BaseFragment
import com.larvaesoft.opengst.utils.GSTHelper
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : BaseFragment() {
    private lateinit var  remoteConfig: FirebaseRemoteConfig
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        return inflater!!.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MobileAds.initialize(activity, getString(R.string.app_id))
        adViewLarge.loadAd(AdRequest.Builder().build())
        setupListeners()

    }

    private fun setupListeners() {
        fab_facebook.setOnClickListener { startFaceBook() }
        fab_share.setOnClickListener { startShare() }
        fab_twitter.setOnClickListener { startTwitter() }
        fab_web.setOnClickListener { startWebsite() }
        rate_us_button.setOnClickListener{startRateApp()}
        rate_us_image.setOnTouchListener { _, e ->
            if (e.action == MotionEvent.ACTION_UP) {
              startRateApp()
            }
            true
        }
    }

    private fun startRateApp() {
        GSTHelper.rateApp(activity)
    }

    private fun startWebsite() {
        GSTHelper.startWebsite(activity)
    }

    private fun startTwitter() {
        GSTHelper.startTwitter(activity)
    }

    private fun startShare() {
        GSTHelper.startShare(activity)
    }

    private fun startFaceBook() {
        GSTHelper.startFacebook(activity)
    }
}