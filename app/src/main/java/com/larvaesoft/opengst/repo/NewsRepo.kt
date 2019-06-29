package com.larvaesoft.opengst.repo

import com.larvaesoft.opengst.api.GoogleNewsApi
import com.larvaesoft.opengst.api.LinkDownloadApi
import com.larvaesoft.opengst.model.Article
import com.larvaesoft.opengst.utils.XMLParser
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import retrofit2.Retrofit
import java.util.ArrayList
import javax.inject.Inject

class NewsRepo @Inject constructor(private val retrofit: Retrofit) {
    private var linkApi: LinkDownloadApi? = null
    private var newsApi: GoogleNewsApi? = null

    private fun getLinkApi(): LinkDownloadApi {
        if (linkApi == null) {
            linkApi = retrofit.create(LinkDownloadApi::class.java)
        }
        return linkApi!!
    }
    private fun getNewsApi(): GoogleNewsApi {
        if(newsApi == null){
            newsApi = retrofit.create(GoogleNewsApi::class.java)
        }
        return newsApi!!
    }
    fun downloadLink(url: String): Observable<String> {
        return getLinkApi().downloadUrl(url)
                .subscribeOn(Schedulers.newThread())
                .map { data ->
                    val doc = Jsoup.parse(data.string())
                    doc.select("meta[property=og:image]").first().attr("content")
                }
    }

    fun getNews(): Observable<ArrayList<Article>> {
        return getNewsApi()
                .getGoogleNews()
                .subscribeOn(Schedulers.newThread())
                .map{ response ->
                    XMLParser().parseXML(response.string())
                }
    }

}