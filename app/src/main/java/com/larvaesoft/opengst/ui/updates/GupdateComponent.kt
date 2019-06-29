package com.larvaesoft.opengst.ui.updates

import com.larvaesoft.opengst.AppComponent
import com.larvaesoft.opengst.di.PerActivity
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class),modules = arrayOf(GupdateModule::class))
interface GupdateComponent {
    fun inject(fragment: GstUpdateFragment)
}