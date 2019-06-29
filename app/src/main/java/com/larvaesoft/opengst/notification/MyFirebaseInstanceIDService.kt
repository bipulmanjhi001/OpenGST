package com.larvaesoft.opengst.notification

import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseInstanceIDService:FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Timber.d("token =" + refreshedToken!!)
        FirebaseMessaging.getInstance().subscribeToTopic("global")
    }

}