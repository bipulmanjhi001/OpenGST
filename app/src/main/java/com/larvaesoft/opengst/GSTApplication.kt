package com.larvaesoft.opengst

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.larvaesoft.opengst.database.AppDb
import com.larvaesoft.opengst.di.AppModule
import retrofit2.Retrofit
import timber.log.Timber
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric



class GSTApplication : Application() {
    private lateinit var remoteConfig: FirebaseRemoteConfig

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(FakeTree())
        }
        FirebaseAnalytics.getInstance(this)
        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        component.inject(this)
        remoteConfig = FirebaseRemoteConfig.getInstance()
        Fabric.with(this, Crashlytics())
        setupRemote()
    }

    private fun setupRemote() {
        val default = hashMapOf<String, Any>()
        default.put(Constants.latestVersion, BuildConfig.VERSION_CODE)
        default.put(Constants.adTime, "30") //ad showing time in second
        default.put(Constants.message,getString(R.string.welcome_message))
        default.put(Constants.pollTime,7200)
        val firebaseSetting = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build()
        remoteConfig.setDefaults(default)
        remoteConfig.setConfigSettings(firebaseSetting)
        remoteConfig.fetch(3600)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        remoteConfig.activateFetched()
                        Timber.d("remote setting fetched")
                    }
                }
    }

}

class FakeTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {

    }
}
