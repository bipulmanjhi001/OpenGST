package com.larvaesoft.opengst.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.larvaesoft.opengst.model.Item
import io.reactivex.Flowable

@Dao
interface ItemsDao {
    @Insert
    fun insertItem(i: Item)

    @Query("SELECT * from items_table")
    fun getAll(): Flowable<List<Item>>

    @Query("SELECT * FROM items_table WHERE description LIKE :text")
    fun search(text: String): Flowable<List<Item>>

    @Query("SELECT * FROM items_table WHERE id IN (:ids)")
    fun getRandomItems(ids:MutableList<Int>):Flowable<List<Item>>
}