package com.larvaesoft.opengst.service


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.google.firebase.analytics.FirebaseAnalytics
import com.larvaesoft.opengst.GSTApplication
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.model.Article
import com.larvaesoft.opengst.repo.NewsRepo
import com.larvaesoft.opengst.ui.main.MainActivity
import com.larvaesoft.opengst.utils.MSharedPref
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import timber.log.Timber

import javax.inject.Inject
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class NotificationScheduler : JobService() {
    @Inject lateinit var newsRepo: NewsRepo
    @Inject lateinit var mSharedPref: MSharedPref
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        GSTApplication.component.inject(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.e("starting job..")
        checkNewsUpdate(params)
        return true
    }

    private fun canShowNotification(t: Article): Boolean {
        Timber.e("article title ===" + t.title)
        val currentHash = getMD5EncryptedString(t.title)
        val previousHash = mSharedPref.getString("news_hash", "")
        Timber.e("previous hash ->" + previousHash)
        Timber.e("current hash ->" + currentHash)
        if (previousHash != currentHash) {
            mSharedPref.put("news_hash", currentHash)
            return true
        }
        return false
    }

    private fun getMD5EncryptedString(encTarget: String): String {
        var mdEnc: MessageDigest? = null
        try {
            mdEnc = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            println("Exception while encrypting to md5")
            e.printStackTrace()
        }
        // Encryption algorithm
        mdEnc!!.update(encTarget.toByteArray(), 0, encTarget.length)
        var md5 = BigInteger(1, mdEnc!!.digest()).toString(16)
        while (md5.length < 32) {
            md5 = "0" + md5
        }
        return md5
    }

    private fun checkNewsUpdate(params: JobParameters?) {
        newsRepo.getNews()
                .subscribeOn(Schedulers.newThread())
                .flatMap { article ->
                    article.toObservable()
                }
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Article> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Article) {
                        Timber.d("got article ")
                        if (canShowNotification(t)) {
                            createNotification(t, params)
                        } else {
                            jobFinished(params!!, false)
                        }
                    }

                    override fun onError(e: Throwable) {
                        toast(e.message.toString())
                    }

                    override fun onComplete() {
                    }
                })
    }

    private fun createNotification(t: Article, params: JobParameters?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("from", "news")
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        notify("GTS Breaking News", t.title, pi, params)
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun notify(title: String, message: String, pendingIntent: PendingIntent, params: JobParameters?) {
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_opengst_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
        Timber.e("notification shown")
        logNotificationEvent()
        jobFinished(params!!, false)
    }

    private fun logNotificationEvent() {
        val param = Bundle()
        param.putString("param", "notification_shown")
        mFirebaseAnalytics.logEvent("notification", param)
    }
}