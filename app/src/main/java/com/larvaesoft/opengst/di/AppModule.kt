package com.larvaesoft.opengst.di

import android.arch.persistence.room.Room
import android.content.Context
import com.larvaesoft.opengst.GSTApplication
import com.larvaesoft.opengst.database.AppDb
import com.larvaesoft.opengst.utils.MSharedPref
import dagger.Module
import dagger.Provides
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val app: GSTApplication) {
    private val DBNAME = "app.db"

    @Singleton
    @Provides
    @Named("AppContext")
    fun providesApplicationContext(): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun providesAppDatabase(@Named("AppContext") context: Context): AppDb {
        try {
            checkDb(context)
        } catch (io: IOException) {
            io.printStackTrace()
        }
        return Room.databaseBuilder(context, AppDb::class.java, DBNAME)
                .build()
    }

    @Throws(IOException::class)
    private fun checkDb(context: Context) {
        if (!context.getDatabasePath(DBNAME).exists()) {
            Timber.e("db does not exists copying db")
            val inputFile = context.assets.open(DBNAME)
            val outputFile = context.getDatabasePath(DBNAME)
            context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null)
            val output = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            while (inputFile.read(buffer) > 0) {
                output.write(buffer)
            }
            output.flush()
            output.close()
            inputFile.close()
            return
        }
        Timber.d("Db exists not copying")
    }

    @Singleton
    @Provides
    fun providesSharedPref(@Named("AppContext") context: Context): MSharedPref =
            MSharedPref(context)

}