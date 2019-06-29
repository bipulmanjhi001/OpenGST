package com.larvaesoft.opengst.repo

import com.larvaesoft.opengst.database.AppDb
import com.larvaesoft.opengst.model.Item
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.Random
import javax.inject.Inject

class ItemsRepo @Inject constructor(private val appDb: AppDb) {

    fun getAllItems(): Flowable<List<Item>> {
        return appDb.getItemDao().getAll()
    }

    fun searchForItem(query: String): Flowable<List<Item>> {
       return appDb.getItemDao().search(query)
                .observeOn(Schedulers.computation())
    }

    fun getInitialData(): Flowable<List<Item>> {
        val ids = mutableListOf<Int>()
        for(i in 0..20){
            ids.add(getRandom())
        }
        return appDb.getItemDao().getRandomItems(ids)
    }
    private fun getRandom(): Int {
        val min =3
        val max= 1300
        return Random().nextInt(max - min) + min
    }
}