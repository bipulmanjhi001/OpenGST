package com.larvaesoft.opengst.ui.updates

import com.larvaesoft.opengst.di.PerActivity
import dagger.Module
import dagger.Provides

@Module
class GupdateModule(private val gUpdateInterface:GupdateInterface) {

    @PerActivity
    @Provides
    fun providesGstInterface(): GupdateInterface {
        return gUpdateInterface
    }
}