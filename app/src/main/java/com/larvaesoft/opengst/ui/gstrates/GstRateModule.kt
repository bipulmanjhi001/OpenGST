package com.larvaesoft.opengst.ui.gstrates

import com.larvaesoft.opengst.di.PerActivity
import dagger.Module
import dagger.Provides

@Module
class GstRateModule(private val gstRateInterface: GstRateInterface) {

    @PerActivity
    @Provides
    fun providesGstInterface(): GstRateInterface {
        return gstRateInterface
    }
}