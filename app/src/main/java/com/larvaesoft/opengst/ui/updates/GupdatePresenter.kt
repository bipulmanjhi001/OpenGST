package com.larvaesoft.opengst.ui.updates

import android.content.Context
import com.larvaesoft.opengst.model.Article
import com.larvaesoft.opengst.repo.NewsRepo
import com.larvaesoft.opengst.ui.browser.BrowserActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.intentFor
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

class GupdatePresenter @Inject constructor(private val gupdateInterface: GupdateInterface, private val newsRepo: NewsRepo) {
    private var compositeDispose: CompositeDisposable = CompositeDisposable()
    private var list = mutableListOf<Article>()
    fun loadData() {
        gupdateInterface.showProgress()
        newsRepo.getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { e ->
                            e.printStackTrace()
                            gupdateInterface.hideProgress()
                            gupdateInterface.showNoUpdatesAvailable()
                        },
                        onNext = { articleList ->
                            gupdateInterface.hideProgress()
                            showArticles(articleList)
                        }
                )
    }

    private lateinit var adapter: FeedAdapter

    private fun showArticles(articleList: ArrayList<Article>) {
        if (articleList.size == 0) return gupdateInterface.showNoUpdatesAvailable()
        list.clear()
        list.addAll(articleList)
        adapter = FeedAdapter(list, this)
        gupdateInterface.setUpAdapter(adapter)
        updateImage()
    }

    fun getContext(): Context {
        return gupdateInterface.getContextFor()
    }

    fun openSource(link: String) {
        val context = gupdateInterface.getContextFor()
        context.startActivity(context.intentFor<BrowserActivity>("url" to link))
    }

    fun disposeAll() {
        compositeDispose.dispose()
    }

    private fun updateImage() {
        Observable.fromArray(list)
                .subscribeOn(Schedulers.newThread())
                .flatMap { list ->
                    list.toObservable()
                }
                .flatMap { article ->
                    newsRepo.downloadLink(article.link)
                            .map { image ->
                                Timber.d("image before == " + article.image)
                                article.image = image
                                article
                            }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { article -> Timber.d("got image " + article.image) },
                        onError = { e -> e.printStackTrace() },
                        onComplete = { adapter.notifyDataSetChanged() })
    }
}