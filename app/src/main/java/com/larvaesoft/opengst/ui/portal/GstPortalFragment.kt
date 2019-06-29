package com.larvaesoft.opengst.ui.portal


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.ui.browser.BrowserActivity
import kotlinx.android.synthetic.main.fragment_gst_portal.*
import org.jetbrains.anko.intentFor


class GstPortalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_gst_portal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        MobileAds.initialize(activity.applicationContext, getString(R.string.app_id))
        val request = AdRequest.Builder()
                //.addTestDevice("8AF196582EF001C05AE6DAC416B8FB35")
                .build()
        adViewBottom.loadAd(request)

        btn_registration.setOnClickListener { loadUrl("https://reg.gst.gov.in/registration/", "GST Registration") }
        btn_challan.setOnClickListener { loadUrl("https://payment.gst.gov.in/payment/", "Create Challan") }
        btn_verify.setOnClickListener { loadUrl("https://services.gst.gov.in/services/searchtp", "Verify GST Number") }
        btn_gst_login.setOnClickListener { loadUrl("https://services.gst.gov.in/services/login", "GST Login") }
        btn_first_login.setOnClickListener { loadUrl("https://services.gst.gov.in/services/newlogin", "First Login") }
        btn_payment_status.setOnClickListener { loadUrl("https://payment.gst.gov.in/payment/trackpayment", "Verify Payment Status") }
    }

    private fun loadUrl(url: String, title: String) {
        activity.startActivity(activity.intentFor<BrowserActivity>("url" to url, "title" to title))
    }

}
