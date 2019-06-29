package com.larvaesoft.opengst.ui.browser

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.webkit.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.larvaesoft.opengst.R

import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.content_browser.*
import timber.log.Timber
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd


class BrowserActivity : AppCompatActivity() {

    private lateinit var videoAd: InterstitialAd

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)
        setSupportActionBar(toolbar)
        MobileAds.initialize(this.applicationContext, getString(R.string.app_id))
        val request = AdRequest.Builder()
               /* .addTestDevice("8AF196582EF001C05AE6DAC416B8FB35")*/
                .build()
        adViewBottom.loadAd(request)

        videoAd = InterstitialAd(this)
        videoAd.adUnitId = "ca-app-pub-1490495698690566/7806241671"
        videoAd.loadAd(AdRequest.Builder().build())

        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.settings.allowContentAccess = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        val i = intent
        val url = i.getStringExtra("url")
        try {
            var title = i.getStringExtra("title")
            if (title == null || title.isEmpty()) {
                title = "OpenGST"
            }
            supportActionBar!!.title = title
        } catch (ex: NullPointerException) {
            supportActionBar!!.title = "OpenGST"
        }
        Timber.d("loading url " + url)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                Timber.d("progress = " + newProgress)
                progressBar2.progress = newProgress
                if (newProgress == 100) {
                    progressBar2.visibility = View.GONE
                } else {
                    progressBar2.visibility = View.VISIBLE
                }
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Timber.d(consoleMessage!!.message())
                return true
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, er: SslError) {
                handler.proceed()
                // Ignore SSL certificate errors
            }
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        if (videoAd.isLoaded) {
            videoAd.show()
            super.onPause()
        } else {
            super.onPause()
        }
        Timber.e("on pause on browser")
    }
}
