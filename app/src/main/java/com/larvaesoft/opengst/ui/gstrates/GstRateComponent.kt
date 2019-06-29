package com.larvaesoft.opengst.ui.gstrates

import com.larvaesoft.opengst.AppComponent
import com.larvaesoft.opengst.di.PerActivity
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(GstRateModule::class))
interface GstRateComponent {
    fun inject(fragment: GstRatesFragment)
}