package com.larvaesoft.opengst.ui.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.firebase.jobdispatcher.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.analytics.FirebaseAnalytics

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.larvaesoft.opengst.BuildConfig
import com.larvaesoft.opengst.Constants
import com.larvaesoft.opengst.GSTApplication
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.service.NotificationScheduler
import com.larvaesoft.opengst.ui.MyFragmentAdapter
import com.larvaesoft.opengst.utils.GSTHelper
import com.larvaesoft.opengst.utils.MSharedPref

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.browse
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    private var fm: FragmentManager? = null
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private var adTime = 0
    private lateinit var fireBase: FirebaseAnalytics
    private var adapter = MyFragmentAdapter(supportFragmentManager)
    @Inject lateinit var sharedPref: MSharedPref
    private var isPaused = false
    private lateinit var dispatcher: FirebaseJobDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        GSTApplication.component.inject(this)
        dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        fireBase = FirebaseAnalytics.getInstance(this)
        fm = supportFragmentManager
        setupBottomNavigation()
        remoteConfig = FirebaseRemoteConfig.getInstance()
        viewPager.adapter = adapter
        viewPager.setPagingEnabled(false)
        try {
            val from = intent.getStringExtra("from")
            if (from.isNotBlank()) {
                loadFragment(2)
                bottom_navigation.currentItem = 2
                val params = Bundle()
                params.putString("from", "news_notification")
                fireBase.logEvent("app_open", params)
            }
        } catch (ex: Exception) {
        }
        initVideoAds()
        checkForUpdate()
        scheduleJob()
    }

    private fun checkForUpdate() {
        val availableVersion = remoteConfig.getString(Constants.latestVersion)
        Timber.d("available version is $availableVersion")
        if (availableVersion.toInt() > BuildConfig.VERSION_CODE) {
            showUpdateDialog()
        }
    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Update")
        builder.setMessage("Update is available. Download to continue")
        builder.setPositiveButton("Update", { _, _ ->
            browse(Constants.playStoreURL)
        })
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        dialog.setOnDismissListener {
            finish()
        }
        Timber.d("Update dialog Shown")
    }

    private fun setupBottomNavigation() {
        //createItems
        val calc = AHBottomNavigationItem(getString(R.string.calculator), R.drawable.calculator)
        val rate = AHBottomNavigationItem(getString(R.string.gst_rates), R.drawable.ticket_percent)
        val gstUpdates = AHBottomNavigationItem(getString(R.string.gst_updates), R.drawable.newspaper)
        val gstPortal = AHBottomNavigationItem(getString(R.string.gst_portal), R.drawable.edge)
        val about = AHBottomNavigationItem("Social", R.drawable.about)
        //adding to navigator
        bottom_navigation.addItem(calc)
        bottom_navigation.addItem(rate)
        bottom_navigation.addItem(gstUpdates)
        bottom_navigation.addItem(gstPortal)
        bottom_navigation.addItem(about)
        bottom_navigation.defaultBackgroundColor = Color.WHITE
        bottom_navigation.accentColor = ContextCompat.getColor(this, R.color.colorAccent)

        bottom_navigation.setOnTabSelectedListener { position, wasSelected ->
            if (!wasSelected) {
                loadFragment(position)
            }
            true
        }
        bottom_navigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottom_navigation.isBehaviorTranslationEnabled = true
    }

    private fun loadFragment(position: Int) {
        viewPager.setCurrentItem(position, false)
        if (supportActionBar != null) {
            supportActionBar!!.title = adapter.getPageTitle(position)
        }
        //logging screen view here else it is logged when fragment is created
        //which when using viewpager happens all at once
        logScreenView(position)
    }

    private fun logScreenView(position: Int) {
        var screenName = ""
        when (position) {
            0 -> screenName = "CalcFragment"
            1 -> screenName = "GSTRateFragment"
            2 -> screenName = "GSTUpdateFragment"
            3 -> screenName = "GSTPortalFragment"
            4 -> screenName = "AboutFragment"
        }
        fireBase.setCurrentScreen(this, screenName, screenName)
    }

    private lateinit var videoAd: InterstitialAd

    private fun initVideoAds() {
        videoAd = InterstitialAd(this)
        videoAd.adUnitId = "ca-app-pub-1490495698690566/7806241671"
        videoAd.loadAd(AdRequest.Builder().build())
        try {
            adTime = remoteConfig.getString(Constants.adTime).toInt()
        } catch (ex: Exception) {

        }
        startCounter()
    }

    private fun startCounter() {
        android.os.Handler().postDelayed({ showVideoAd() }, adTime * 1000.toLong())
    }

    private fun showVideoAd() {
        if (videoAd.isLoaded && !isPaused) {
            videoAd.show()
            startCounter()
        } else {
            startCounter()
        }
    }

    override fun onBackPressed() {
        if (videoAd.isLoaded) {
            videoAd.show()
            videoAd.adListener = object : AdListener() {
                override fun onAdClosed() {
                    finish()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_facebook -> GSTHelper.startFacebook(this)
            R.id.action_twitter -> GSTHelper.startTwitter(this)
            R.id.action_web -> GSTHelper.startWebsite(this)
            R.id.action_rate -> GSTHelper.rateApp(this)
            R.id.action_share -> GSTHelper.startShare(this)
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
    }

    private fun scheduleJob() {
        val remoteMaxTime = remoteConfig.getString(Constants.pollTime).toInt()
        val previousTime = sharedPref.getInt("maxTime",7200)
        if (isFirstRun() || previousTime != remoteMaxTime) {
            val job = dispatcher.newJobBuilder()
                    .setService(NotificationScheduler::class.java)
                    .setRecurring(true)
                    .setReplaceCurrent(true)
                    .setTag("notification_tag")
                    .setTrigger(Trigger.executionWindow(3600, remoteMaxTime))
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build()
            dispatcher.mustSchedule(job)
            sharedPref.put("maxTime",remoteMaxTime)
        }
    }

    private fun isFirstRun(): Boolean = sharedPref.getBoolean(Constants.isFirstRun, true)
}
