package com.larvaesoft.opengst.notification

import android.app.Notification
import com.google.firebase.messaging.FirebaseMessagingService
import timber.log.Timber
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.crashlytics.android.Crashlytics
import com.larvaesoft.opengst.BuildConfig
import com.larvaesoft.opengst.R
import com.larvaesoft.opengst.ui.main.MainActivity
import com.larvaesoft.opengst.utils.GSTHelper


class MyMessagingService : FirebaseMessagingService() {
    private val typeURL = "url"
    private val typeAPP = "app"
    private val typeFB = "fb"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        Timber.d(remoteMessage!!.messageType)
        if (remoteMessage.notification != null) {
            if (remoteMessage.notification!!.body != null) {
                sendNotification(remoteMessage.notification!!.body!!, remoteMessage.notification!!.title)
            }
            Timber.e("simple notification msg")
        }
        if (remoteMessage.data.isNotEmpty()) {
            handleDataMsg(remoteMessage)
            Timber.e("data message")
        }


    }

    private fun sendNotification(messageBody: String, title: String?) {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT)

            val notificationBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_opengst_notification)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notificationBuilder.build())
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
        }

    }

    private fun handleDataMsg(remoteMessage: RemoteMessage) {
        try {
            val data = remoteMessage.data
            val type = data["type"]
            val title = data["title"]
            val message = data["message"]
            var version = 0
            try {
                version = data["version"]!!.toInt()
                if (version == BuildConfig.VERSION_CODE) {
                    return
                }
            } catch (ex: Exception) {

            }
            when (type) {
                typeURL -> {
                    val url = data["url"]
                    val i = Intent()
                    i.action = Intent.ACTION_VIEW
                    i.data = Uri.parse(url)
                    val pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT)
                    notify(title, message, pendingIntent)
                }

                typeAPP -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    notify(title, message, pi)
                }

                typeFB -> {
                    val fbIntent = Intent(Intent.ACTION_VIEW)
                    val facebookUrl = GSTHelper.getFacebookPageURL(this)
                    fbIntent.data = Uri.parse(facebookUrl)
                    val fbPi = PendingIntent.getActivity(this, 0, fbIntent, PendingIntent.FLAG_ONE_SHOT)
                    notify(title, message, fbPi)
                }
            }
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
        }


    }

    private fun notify(title: String?, message: String?, pendingIntent: PendingIntent) {
        try {
            Timber.e("building notification")
            val notificationBuilder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_stat_opengst_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            }
            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notificationBuilder.build())
            Timber.e("notification shown")
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
        }

    }


}







