package com.larvaesoft.opengst.ui.gstrates

import com.google.android.gms.ads.formats.NativeAd
import com.larvaesoft.opengst.model.Item
import com.larvaesoft.opengst.model.MyItem
import com.larvaesoft.opengst.repo.ItemsRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class GstRatePresenter @Inject constructor(private val gstRateInterface: GstRateInterface, private val repo: ItemsRepo) {
    private val list = mutableListOf<MyItem>()
    private val adList = mutableListOf<NativeAd>()
    private var maxAds: Int = 10
    private var adapter: ResultAdapter? = null

    fun search(query: String?) {
        if (query.isNullOrBlank()) {
            Timber.d("query null returning")
            return
        }
        val q = "%$query%"
        Timber.d("query change to " + q)
        repo.searchForItem(q)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { list -> updateAdapter(list, query) },
                        onError = { e -> e.printStackTrace() }
                )
    }

    private fun updateAdapter(t: List<Item>, query: String?) {
        gstRateInterface.logSearchQuery(query)
        Timber.d("list size = " + t.size)
        list.clear()
        list.addAll(t)
        if (t.isEmpty()) {
            gstRateInterface.showNoResult()
        } else {
            adapter = ResultAdapter(list, this, query)
            gstRateInterface.setUpAdapter(adapter!!)
            gstRateInterface.showResult()
            gstRateInterface.showTotalResults(t.size)
        }
    }

    fun showRateDetail(item: Item) {
        gstRateInterface.showDetailDialog(item)
    }

    fun init() {
        repo.getInitialData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = {e -> e.printStackTrace()},
                        onNext = { l->
                            showInititalData(l)
                        }
                )
    }
    private fun showInititalData(l: List<Item>) {
        list.clear()
        list.addAll(l)
        adapter = ResultAdapter(list, this, "")
        gstRateInterface.setUpAdapter(adapter!!)
        gstRateInterface.showResult()
    }

}