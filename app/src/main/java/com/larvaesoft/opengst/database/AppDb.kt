package com.larvaesoft.opengst.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.larvaesoft.opengst.model.Item

@Database(entities = arrayOf(Item::class),version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun getItemDao():ItemsDao
}