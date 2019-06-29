package com.larvaesoft.opengst.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseFragment : Fragment() {
    lateinit var fireBaseAnalytics:FirebaseAnalytics

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fireBaseAnalytics= FirebaseAnalytics.getInstance(activity)
    }

}