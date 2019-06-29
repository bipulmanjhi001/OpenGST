package com.larvaesoft.opengst

import android.content.Context
import com.larvaesoft.opengst.database.AppDb
import com.larvaesoft.opengst.di.AppModule
import com.larvaesoft.opengst.di.NetModule
import com.larvaesoft.opengst.service.NotificationScheduler
import com.larvaesoft.opengst.ui.main.MainActivity
import com.larvaesoft.opengst.utils.MSharedPref
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class,NetModule::class))
interface AppComponent {
    fun getAppDb():AppDb
    fun getRetrofit():Retrofit
    fun getMSharedPref():MSharedPref
    fun inject(app: GSTApplication)
    fun inject(service:NotificationScheduler)
    fun inject(activity:MainActivity)
}